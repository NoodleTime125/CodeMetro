package codemetro.visualizer;

//PApplet
import processing.core.PApplet;

//Unfolding
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.*;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.providers.*; //Map Styles
import de.fhpotsdam.unfolding.utils.MapUtils;
import de.fhpotsdam.unfolding.marker.*;

//Ani
import de.looksgood.ani.*;

//Java Default Libraries
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;
import java.util.ArrayList;

public class MapPlot extends PApplet{
	private static final long serialVersionUID = 6879860210379903374L;
	UnfoldingMap metro;
	float x = 0;
	float y = 0;
	MarkerManager<Marker> trainManager = new MarkerManager<Marker>();
	
	public void setup() {
		size(1280, 720); //setup size of applet
		smooth(); //anti-aliase edges
		//noStroke(); //TODO disables drawing of edges?? wat		
		Ani.init(this); //initialize Ani, our train moving visualizer...		
		metro = new UnfoldingMap(this, new Google.GoogleSimplifiedProvider()); //TODO using GoogleSimplified2Provider for now...find a better map style
		String JSONLineFile = "mockLine.json";
		String JSONMarkerFile = "mockMarker.json";
		
		
		// Add mouse and keyboard interactions 
		//TODO Remove in the future, we don't want users to fiddle with the map.
        MapUtils.createDefaultEventDispatcher(this, metro);
		
		//Show particular location in world map
		//Location vancouver = new Location(49.27f,-123.1f);
		Location testMap = new Location(0f,0f);
		int zoomLv = 6; //the level of zoom for the map (0 = world view, bigger number = more zoom) use 12
		metro.zoomAndPanTo(zoomLv, testMap);
		
		List<Feature> loFL = GeoJSONReader.loadData(this, JSONLineFile);
		final MultiFeature mF = plotLines(metro, loFL); //TODO final is okay..?
		
		List<Feature> loF = GeoJSONReader.loadData(this, JSONMarkerFile);
		final List<Marker> loL = plotPoints(metro, loF);
		
		SimplePointMarker train1 = new SimplePointMarker(new Location(0f, 1f));
		train1.setColor(color(0,0,0));
		metro.addMarker(train1);
		trainManager.addMarker(train1);
		
		SimplePointMarker train2 = new SimplePointMarker(new Location(0f, 1f));
		train1.setColor(color(0,0,0));
		metro.addMarker(train2);
		trainManager.addMarker(train2);
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				x = 0;
				y = 0;
				moveTrains(trainManager, mF);
			}
		}, 1000, 5000);
	}
	
	public MultiFeature listToMultiFeat(List<Feature> loF) { //Makes List<Feature> into MultiFeature
		MultiFeature multiFeature = new MultiFeature();
		for (int i = 0; i < loF.size(); i++) {
			multiFeature.addFeature(loF.get(i));
		}
		return multiFeature;
	}
	
	public List<Marker> plotPoints(UnfoldingMap metro, List<Feature> loF) {
		//Plot Points
		List<Marker> loM = MapUtils.createSimpleMarkers(loF);
		SimplePointMarker simpleMarker;
		for (int index = 0; index < loM.size(); index++) {
			simpleMarker = new SimplePointMarker(loM.get(index).getLocation());
			simpleMarker.setStrokeWeight(3);
			simpleMarker.setStrokeColor(color(0,0,0)); //Black
			simpleMarker.setColor(color(255,255,255)); //White
			
			metro.addMarker(simpleMarker);
			//System.out.println(loM.get(index).getLocation());
		}
		return loM;
	}
	
	public MultiFeature plotLines(UnfoldingMap metro, List<Feature> loFL) {
		//Plot Lines
		//List<List<Location>> loloL = new ArrayList<List<Location>>();
		MultiFeature mF = listToMultiFeat(loFL);

		for (int index = 0; index < loFL.size(); index++) { //for each polyline set
			List<Location> loL = new ArrayList<Location>();
			ShapeFeature line = (ShapeFeature) mF.getFeatures().get(index);

			lineToSubwayLine(line); //converts lines to Subway-style lines

			//System.out.println("Still in for loop\n");

			//Plot lines onto map and add characteristics to lines
			SimpleLinesMarker lineMarker = new SimpleLinesMarker(line.getLocations());
			lineMarker.setStrokeWeight(5);
			lineMarker.setColor(color(255,0,0)); //RED
			
			for (int i = 0; i < line.getLocations().size(); i++) {
				loL.add(line.getLocations().get(i));
			}
			mF.addFeature(line); //each mF is exactly one line
			metro.addMarker(lineMarker);
		}
		return mF;
	}
	
	public void lineToSubwayLine(ShapeFeature linePoint) {	//turn lines into subway-like paths
		int nextLocation = 1;
		for (int i = 0; i < linePoint.getLocations().size() - nextLocation; i++){ //we compare the current latlon with next latlon, so we minus nextLocation to handle array out of bound
			float currentLat = linePoint.getLocations().get(i).getLat();
			float currentLon = linePoint.getLocations().get(i).getLon();

			//System.out.println(linePoint.getLocations().size()); //TODO remove
			float nextLat = linePoint.getLocations().get(i+1).getLat();
			float nextLon = linePoint.getLocations().get(i+1).getLon();
			if ((currentLat != nextLat)&&(currentLon != nextLon)){ //Lines will be diagonal, force lines to 45-degree angles
				if((Math.abs(currentLat - nextLat)) != (Math.abs(currentLon - nextLon))){ //checks if lines are not 45-degrees by nature
					//System.out.println("hey, not 45 degrees!\n");
					//float tempLatDist = Math.abs(currentLat - nextLat);
					float tempLat;
					float tempLon = currentLon;
					tempLat = Math.abs(nextLon - currentLon);
					Location tempLatLon = null;
					
					//System.out.println("\ntempLat " + tempLat);
					//System.out.println("CLat " + currentLat + " Lon " + currentLon); //TODO remove
					//System.out.println("NLat " + nextLat + " NLon " + nextLon);
					if (Math.abs(currentLat) - Math.abs(nextLat) < Math.abs(currentLon) - Math.abs(nextLon)) { //if latitude is less than longitude
						if (nextLon > currentLon) {
							tempLatLon = new Location(currentLat + tempLat, tempLon); //this is fine
							//System.out.println("if else 1");
						} else {
							tempLatLon = new Location(tempLat, tempLon - nextLon); //this is fine
							//System.out.println("if else 2");
						}
					} else
						if (nextLon > currentLon) {
							//System.out.println("if else 3");
							tempLatLon = new Location(nextLat, Math.abs(currentLat - nextLat));
						}
						else { 
							tempLatLon = new Location(tempLat, tempLon);
							//System.out.println("if else 4");
						}

					//now we insert this tempLatLon into our linePoint and recursively move all linePoints + 1
					linePoint.getLocations().add(i+1, tempLatLon);
				}
			}
			
		}
	}
	
	
	public void moveTrains(MarkerManager<Marker> trainManager, MultiFeature mF) {
		System.out.println(trainManager.getMarkers().get(0).getId());
		System.out.println(trainManager.getMarkers().get(1).getId());
		for (int i = 0; i < trainManager.getMarkers().size(); i++) {
			for (int n = 0; n < mF.getFeatures().size(); n++) {
				ShapeFeature sF = (ShapeFeature) mF.getFeatures().get(n);
				for (int k = 0; k < sF.getLocations().size(); k++) {
					Ani.to(this, 6.0f, "x", sF.getLocations().get(k).x);	
					Ani.to(this, 6.0f, "y", sF.getLocations().get(k).y);
				}
			}
		}
	}
	
	public void draw() {
		metro.draw(); //draw map
		
		for (int i = 0; i < trainManager.getMarkers().size(); i++) {
			if (i == 0)
				trainManager.getMarkers().get(i).setLocation(y, x);
			else
				trainManager.getMarkers().get(i).setLocation(y, x);
		}
	}
}
