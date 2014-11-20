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

import java.util.Collections;
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
	List<Feature> allLoFL = Collections.synchronizedList(new ArrayList<Feature>()); //contains all the Features in a List
	List<MultiFeature> newlyadded = new ArrayList<MultiFeature>(); //contains the features that we will add to the current metro map
	MultiFeature wayPoints = new MultiFeature(); //contains the wayPoints for all the lines
	Random rand = new Random(100);
	int year = 1980;
	int finalyear = 0;
	int yearcounter = 0;
	String completed = "Subway construction completed";
	String yearStr = Integer.toString(year);
	String name = "Testing";
	boolean modifying = false;
	
	/**
	 * Setting up the visualizer
	 */
	public void setup(List<MultiFeature> loMF) {
		size(1280, 720, P2D); //setup size of applet, using OpenGL's PGraphics2D as the renderer
		smooth(); //anti-aliase edges
		noStroke();	
		Ani.init(this); //initialize Ani, our train moving visualizer...	
		Ani.setDefaultEasing(Ani.QUAD_IN_OUT);
		metro = new UnfoldingMap(this, new Google.GoogleSimplifiedProvider()); //TODO using GoogleSimplified2Provider for now...find a better map style
		
		//testmethod();
		
		for (int i = 0; i < loMF.size(); i++) {
			addLine(loMF.get(i));
		}

		
		// Add mouse and keyboard interactions 
		//TODO Remove in the future, we don't want users to fiddle with the map.
        MapUtils.createDefaultEventDispatcher(this, metro);
		
		//Show particular location in world map
		Location testMap = new Location(0f,0f);
		int zoomLv = 6; //the level of zoom for the map (0 = world view, bigger number = more zoom) use 12
		metro.zoomAndPanTo(zoomLv, testMap);
		List<Feature> loFL = allLoFL;

		run(loFL, wayPoints); //runs the trains across the lines and counts the year
	}
	
	private void testmethod() { //TODO remove later when Angelo's stuff works with MapPlot
		List<Feature> test; //TODO remove later when Angelo's stuff works
		String JSONLineFile = "mockLine.json"; //TODO remove later when Angelo's stuff works
		test = GeoJSONReader.loadData(this, JSONLineFile); //TODO remove later when Angelo's stuff works
		
		ShapeFeature testsf = new ShapeFeature(Feature.FeatureType.LINES);
		testsf.addLocation(new Location(10,10));
		testsf.addLocation(new Location(5,5));
		
		MultiFeature mfeat = new MultiFeature();
		MultiFeature mfeat2 = new MultiFeature();
		MultiFeature mfeat3 = new MultiFeature();
			
		mfeat.addFeature(test.get(0));
		mfeat.addFeature(test.get(1));
		addLine(mfeat);
			
		mfeat2.addFeature(test.get(2));
		mfeat2.addFeature(test.get(3));
		mfeat2.addFeature(testsf);
		addLine(mfeat2);
			
		mfeat3.addFeature(test.get(4));
		addLine(mfeat3);
	}

	private void linetoStation(List<Feature> loFL) { //gets the stations from a line

		for (int i = 0; i < loFL.size() ; i++) {
			ShapeFeature wP = (ShapeFeature) loFL.get(i);
			for (int j = 0; j < wP.getLocations().size() ; j++) {
				if (j == 0 || j == wP.getLocations().size() -1) {
					plotPoint(metro, wP.getLocations().get(j));
				}
			}
		}
	}
	
	private void addTrain(MultiFeature wayPoints, List<Feature> loF) { //puts a train onto that line
		//plotPoints(metro, loF); //plots the list of feature markers (loF) onto Unfolding Map(metro)
		for (int i = 0; i < loF.size(); i++) {
			ShapeFeature sF = (ShapeFeature) wayPoints.getFeatures().get(i); //convert feature in wayPoints to shapefeature
			Train train = new Train(sF.getLocations().get(0)); //initialize train at beginning of the line
			train.getTrain().setColor(color(0,0,0));
			trainManager.add(train); //add train to trainManager
			train.setloF(loF);
			train.setMultiFeat(wayPoints);
			train.setWayPoints(wayPoints.getFeatures().get(i)); //add waypoints of where the train needs to go (the line)
			metro.addMarker(train.getTrain()); //add train to the map
		}
	}
	
	private synchronized void run(List<Feature> allLoFL, final MultiFeature wayPoints) {
		Timer timer = new Timer();
		timer.schedule(new VariableTimerTask(trainManager) {
			@Override
			public synchronized void run() {
				modifying = true;
				if (newlyadded.size() > 0 && yearcounter == 0) { //adds the newly added line to the map
					List<Feature> templist = new ArrayList<Feature>(); 
					for (int i = 0; i < newlyadded.get(0).getFeatures().size(); i++) {
						templist.add(newlyadded.get(0).getFeatures().get(i));
					}
					synchronized (metro) {
					MultiFeature tempmf = plotLines(metro, templist); //converts templist to subway style lines and plots them onto Unfolding Map (metro)
					linetoStation(templist); //takes in a line and plots stations at the beginning and end of the lines
					addTrain(tempmf, templist); //adds a train to each line
					}
					for (int i = 0; i < newlyadded.get(0).getFeatures().size(); i++) {
						wayPoints.addFeature(newlyadded.get(0).getFeatures().get(i));
					}
					newlyadded.remove(0);

				}
				for (int i = 0; i < trainManager.size(); i++) { //moves each train to the next waypoint
					Train train = trainManager.get(i);
					if (train.getIndexWayPoint() == 0) {
						train.setX(train.getLocations().get(0).x);
						train.setY(train.getLocations().get(0).y);
					}
					//System.out.println("[" + train.getX() + ", " + train.getY() + "]");
					//System.out.println(wayPoints.getFeatures().size());
					ShapeFeature sF = (ShapeFeature) wayPoints.getFeatures().get(i);
					//System.out.println(sF.getLocations().get(train.getIndexWayPoint()));
					int result = moveTrains(train, sF.getLocations().get(train.getIndexWayPoint()));
					if (result == -1) { //TODO
						
					}
					train.setIndexWayPoint(train.getIndexWayPoint() + 1);
					if (train.getIndexWayPoint() >= train.getLocations().size()-1){
						train.setIndexWayPoint(0);
					}
				}
				yearcounter++;
				if (yearcounter == 2) {
					year++;
					yearcounter = 0;
				}
				yearStr = Integer.toString(year);
				modifying = false;
			}
		}, 2500, 4050);
	}
	
	public void addLine(MultiFeature mfeat) { //Angelo will call this method to add a line to allLoFL, the master lines holder
		MultiFeature temp = new MultiFeature();
		for (int i = 0; i < mfeat.getFeatures().size(); i++) {
			ShapeFeature sf = (ShapeFeature) mfeat.getFeatures().get(i);
			temp.addFeature(sf);
		}
		newlyadded.add(temp);
	}
	
	/**
	 * Converts List<Features> to MultiFeature class
	 * @param loF list of features
	 * @return MultiFeature class
	 */
	private MultiFeature listToMultiFeat(List<Feature> loF) { //Converts List<Feature> into MultiFeature
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
	private void plotPoint(UnfoldingMap metro, Location loc) {
		//Plot Points
		SimplePointMarker simpleMarker;
		simpleMarker = new SimplePointMarker(loc);
		simpleMarker.setStrokeWeight(3);
		simpleMarker.setStrokeColor(color(0,0,0)); //Black
		simpleMarker.setColor(color(255,255,255)); //White

		metro.addMarker(simpleMarker);
	}
	
	public MultiFeature plotLines(UnfoldingMap metro, List<Feature> loFL) { //loFL is the list of points we want as a line
		//Plot Lines
		MultiFeature mF = listToMultiFeat(loFL);

		for (int index = 0; index < loFL.size(); index++) { //for each polyline set
			ShapeFeature line = (ShapeFeature) mF.getFeatures().get(index);
			line = lineToSubwayLine(line); //converts lines to Subway-style lines
			
			//Plot lines onto map and add characteristics to lines
			SimpleLinesMarker lineMarker = new SimpleLinesMarker(line.getLocations());
			lineMarker.setStrokeWeight(5);
			lineMarker.setColor(color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255))); //just some random color

			mF.addFeature(line); //each mF is exactly one line
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
				float riserun; //uses a rise over run algorithm to plot 45 degree angles
				if (currentLon < nextLon) {
					riserun = ((Math.abs(nextLat - currentLat)) / (Math.abs(nextLon - currentLon)));
				} else {
					riserun = ((Math.abs(currentLat - nextLat)) / (Math.abs(currentLon - nextLon)));
				}
				if (riserun != 1) {
					if (riserun > 1) { //45degrees to 90degrees exclusive
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
				}
			} else { //a base case, vertical, horizontal, or a point as a line goes here
				if ((currentLat != nextLat)&&(currentLon == nextLon)) { //a vertical line
				} else if ((currentLat == nextLat) && (currentLon != nextLon)) { //a horizontal line
				} else {
				}
			}
			
		}
		return linePoint;
	}
	
	private synchronized int moveTrains(Train train, Location loc) {
		return train.moveTrain(this);	
	}
	
	private String subwaystatus() {
		if (newlyadded.size() == 0) {
			if (finalyear == 0) {
				finalyear = year;
			}
			return completed + " in the year " + finalyear;
		} else {
			return "";
		}
	}
	
	/**
	 * Updates and draw the map using the draw() method of UnfoldingMap class
	 * Puts a list of train with their respective coordinates
	 */
	public synchronized void draw() {
		//System.out.println(modifying);
		if (modifying == false) {
			synchronized (metro) {
				metro.draw(); //draw map
			}
		}
		/*
		textSize(35);
		fill(color(255,255,255));
		text("City name: " + name, 10, 40);
		text("Year: " + year, 1100, 40);
		*/ //TODO add a background for text files
		textSize(30);
		fill(color(0,0,0));
		text("City name: " + name, 10, 40);
		text("Year: " + year, 1100, 40); //TODO use relative screen position
		text(subwaystatus(), 300, 700);
		
		for (int i = 0; i < trainManager.size(); i++) {
			Train train = trainManager.get(i);
			train.getMarker().setLocation(train.getLoc());
			//train.getMarker().setLocation(train.getX(), train.getY());
		}
	}
}
