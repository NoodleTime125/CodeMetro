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
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class MapPlot extends PApplet{
	
	// Instant fields and constants
	private static final long serialVersionUID = 6879860210379903374L;
	UnfoldingMap metro;
	float x = 0;
	float y = 0;
	List<Train> trainManager = new ArrayList<Train>();
	List<Feature>  allLoFL;
	Random rand = new Random(100);
	int year = 0;
	String yearStr = Integer.toString(year);
	String name = "Testing";
	
	/**
	 * Setting up the visualizer
	 */
	public void setup() {
		size(1280, 720, P2D); //setup size of applet, using OpenGL's PGraphics2D as the renderer
		smooth(); //anti-aliase edges
		//noStroke(); //TODO disables drawing of edges?? wat		
		Ani.init(this); //initialize Ani, our train moving visualizer...	
		Ani.setDefaultEasing(Ani.QUAD_IN_OUT);
		metro = new UnfoldingMap(this, new Google.GoogleSimplifiedProvider()); //TODO using GoogleSimplified2Provider for now...find a better map style
		String JSONLineFile = "mockLine.json";
		//String JSONMarkerFile = "mockMarker.json";
		
		// Add mouse and keyboard interactions 
		//TODO Remove in the future, we don't want users to fiddle with the map.
        MapUtils.createDefaultEventDispatcher(this, metro);
		
		//Show particular location in world map
		//Location vancouver = new Location(49.27f,-123.1f);
		Location testMap = new Location(0f,0f);
		int zoomLv = 6; //the level of zoom for the map (0 = world view, bigger number = more zoom) use 12
		metro.zoomAndPanTo(zoomLv, testMap);
		
		allLoFL = GeoJSONReader.loadData(this, JSONLineFile);
		
		Timer featureTimer = new Timer();
		featureTimer.schedule(new FeatureTimerTask(allLoFL) {
			@Override
			public void run() {
				//run(allLoFL);
			}
		}, 2000, 4075);
		List<Feature> loFL = allLoFL;
		linetoStation(loFL);
		MultiFeature wayPoints = addTrain(loFL);
		run(loFL, wayPoints);
	}
	
	public void linetoStation(List<Feature> loFL) { //gets the stations from a line

		for (int i = 0; i < loFL.size() ; i++) {
			ShapeFeature wP = (ShapeFeature) loFL.get(i);
			for (int j = 0; j < wP.getLocations().size() ; j++) {
					plotPoint(metro, wP.getLocations().get(j));
			}
		}
	}
	
	public MultiFeature addTrain(List<Feature> loFL) {
		MultiFeature wayPoints = plotLines(metro, loFL); //plots a list of feature lines (loFL) onto Unfolding Map (metro)

		//plotPoints(metro, loF); //plots the list of feature markers (loF) onto Unfolding Map(metro)
		for (int i = 0; i < loFL.size(); i++) {
			ShapeFeature sF = (ShapeFeature) wayPoints.getFeatures().get(i); //convert feature in wayPoints to shapefeature
			Train train = new Train(sF.getLocations().get(0)); //initialize train at beginning of the line
			train.getTrain().setColor(color(0,0,0));
			trainManager.add(train); //add train to trainManager
			train.setWayPoints(wayPoints.getFeatures().get(i)); //add waypoints of where the train needs to go (the line)
			metro.addMarker(train.getTrain()); //add train to the map
		}
		
		return wayPoints;
	}
	
	public void run(List<Feature> allLoFL, final MultiFeature wayPoints) {	
		Timer timer = new Timer();
		timer.schedule(new VariableTimerTask(trainManager) {
			@Override
			public void run() {
				for (int i = 0; i < trainManager.size(); i++) {
					Train train = trainManager.get(i);
					if (train.getIndexWayPoint() == 0) {
						train.setX(train.getLocations().get(0).x);
						train.setY(train.getLocations().get(0).y);
					}
					ShapeFeature sF = (ShapeFeature) wayPoints.getFeatures().get(i);
					//System.out.println(sF.getLocations().get(train.getIndexWayPoint()));
					moveTrains(train, sF.getLocations().get(train.getIndexWayPoint()));
					train.setIndexWayPoint(train.getIndexWayPoint() + 1);
					if (train.getIndexWayPoint() >= train.getLocations().size()-1){
						train.setIndexWayPoint(0);
					}
				}
				year++;
				yearStr = Integer.toString(year);
			}
		}, 2000, 4050);
	}
	
	public void addLine(Feature feat) {
		//TODO angelo calls this to me and i will plot the line onto the map
		//allLoFL.
	}
	
	/**
	 * Converts List<Features> to MultiFeature class
	 * @param loF list of features
	 * @return MultiFeature class
	 */
	public MultiFeature listToMultiFeat(List<Feature> loF) { //Converts List<Feature> into MultiFeature
		MultiFeature multiFeature = new MultiFeature();
		for (int i = 0; i < loF.size(); i++) {
			multiFeature.addFeature(loF.get(i));
		}
		return multiFeature;
	}
	
	/**
	 * Plot the points with the given parameters
	 * @param metro UnfoldingMap class
	 * @param loF list of features
	 * @return List<Marker> 
	 */
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
			//System.out.println("LOM " + loM.get(index).getLocation());
		}
		return loM;
	}
	
	/**
	 * Plot the point with the given parameters
	 * @param metro UnfoldingMap class
	 * @param feat a feature
	 * @return void 
	 */
	public void plotPoint(UnfoldingMap metro, Location loc) {
		//Plot Points
		SimplePointMarker simpleMarker;
		simpleMarker = new SimplePointMarker(loc);
		simpleMarker.setStrokeWeight(3);
		simpleMarker.setStrokeColor(color(0,0,0)); //Black
		simpleMarker.setColor(color(255,255,255)); //White

		metro.addMarker(simpleMarker);
	}
	
	public MultiFeature plotLines(UnfoldingMap metro, List<Feature> loFL) {
		//Plot Lines
		//List<List<Location>> loloL = new ArrayList<List<Location>>();
		MultiFeature mF = listToMultiFeat(loFL);

		for (int index = 0; index < loFL.size(); index++) { //for each polyline set
			List<Location> loL = new ArrayList<Location>();
			ShapeFeature line = (ShapeFeature) mF.getFeatures().get(index);
			line = lineToSubwayLine(line); //converts lines to Subway-style lines
			
			//Plot lines onto map and add characteristics to lines
			SimpleLinesMarker lineMarker = new SimpleLinesMarker(line.getLocations());
			lineMarker.setStrokeWeight(5);
			lineMarker.setColor(color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255))); //just some random color
			
			for (int i = 0; i < line.getLocations().size(); i++) {
				loL.add(line.getLocations().get(i));
			}
			mF.addFeature(line); //each mF is exactly one line
			//System.out.println(line.getLocations().get(1));
			metro.addMarker(lineMarker);
		}
		return mF;
	}
	
	public ShapeFeature lineToSubwayLine(ShapeFeature linePoint) {	//turn lines into subway-like paths
		int nextLocation = 1;
		for (int i = 0; i < linePoint.getLocations().size() - nextLocation; i++){ //we compare the current latlon with next latlon, so we minus nextLocation to handle array out of bound
			float currentLat = linePoint.getLocations().get(i).getLat();
			float currentLon = linePoint.getLocations().get(i).getLon();
			float nextLat = linePoint.getLocations().get(i + nextLocation).getLat();
			float nextLon = linePoint.getLocations().get(i + nextLocation).getLon();

			if ((currentLat != nextLat)&&(currentLon != nextLon)){ //checks if current and next are not the same point
				float riserun;
				//System.out.println();
				if (currentLon < nextLon) {
					riserun = ((Math.abs(nextLat - currentLat)) / (Math.abs(nextLon - currentLon)));
					//System.out.println("currentLon < nextLon");
				} else {
					//System.out.println("currentLon > nextLon");
					riserun = ((Math.abs(currentLat - nextLat)) / (Math.abs(currentLon - nextLon)));
				}
				//float epsilon = 0.001f; //for float comparison
				//System.out.println(riserun + " riserun");
				if (riserun != 1) {
					//System.out.println(currentLon + ", " + currentLat + "    " + nextLon + ", " + nextLat);
					//System.out.println("not 45 degrees!" + riserun);
					if (riserun > 1) { //45degrees to 90degrees exclusive
						//System.out.println("45-90!");
						float tempLon = nextLon;
						float tempLat;
						if (currentLon < nextLon) {
							if (currentLat < nextLat) {
								tempLat = nextLat - Math.abs(nextLon - currentLon);
							} else { //currentLat > nextLat
								tempLat = nextLat + Math.abs(nextLon - currentLon);
							}
						} else {
							if (currentLat < nextLat) {
								tempLat = currentLat + Math.abs(currentLon - nextLon);
							} else { //currentLat > nextLat
								tempLat = currentLat - Math.abs(currentLon - nextLon);
							}
						}
						linePoint.getLocations().add(i+1, new Location(tempLat, tempLon));
					} else {//0degrees to 45degrees exclusive
						//System.out.println("0-45!");
						float tempLon;
						float tempLat = nextLat;
						if (currentLon < nextLon) {
							if (currentLat < nextLat) {
								tempLon = currentLon + Math.abs(currentLat - nextLat);
							} else { //currentLat > nextLat
								tempLon = currentLon + Math.abs(currentLat - nextLat);
							}
						} else { //currentLon > nextLon
							if (currentLat < nextLat) {
								tempLon = currentLon - Math.abs(currentLat - nextLat);
							} else { //currentLat> nextLat
								tempLon = currentLon - Math.abs(currentLat - nextLat);
							}
						}
						linePoint.getLocations().add(i+1, new Location(tempLat, tempLon));
					}
				} else { //a base case
					//System.out.println(currentLon + ", " + currentLat + "    " + nextLon + ", " + nextLat);
					//System.out.println("These two points are 45 degrees already!");
				}
			} else { //a base case
				if ((currentLat != nextLat)&&(currentLon == nextLon)) { //a vertical line
					//System.out.println(currentLon + ", " + currentLat + "    " + nextLon + ", " + nextLat);
					//System.out.println("These two points are verticle!");
				} else if ((currentLat == nextLat) && (currentLon != nextLon)) { //a horizontal line
					//System.out.println(currentLon + ", " + currentLat + "    " + nextLon + ", " + nextLat);
					//System.out.println("These two points are horizontal!");
				} else {
					//System.out.println("These two points are the same point!");
				}
			}
			
		}
		return linePoint;
	}
	
	public void moveTrains(Train train, Location loc) {
		train.moveTrain(this);	
	}
	
	/**
	 * Updates and draw the map using the draw() method of UnfoldingMap class
	 * Puts a list of train with their respective coordinates
	 */
	public void draw() {
		metro.draw(); //draw map
		
		textSize(30);
		fill(color(0,0,0));
		text("City name: " + name, 10, 40);	
		text("Year: " + year, 1100, 40);
		
		for (int i = 0; i < trainManager.size(); i++) {
			Train train = trainManager.get(i);
			train.getMarker().setLocation(train.getLoc());
			//train.getMarker().setLocation(train.getX(), train.getY());
		}
	}
}
