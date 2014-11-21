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

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
//Java Default Libraries
import java.util.Timer;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import codemetro.CodeMetro;
import codemetro.analyzer.callgraph.CallGraphEdge;
import codemetro.analyzer.callgraph.CallGraphNode;
import codemetro.analyzer.gitinspector.GitInspectorEntry;

public class MapPlot extends PApplet{
	
	// Instant fields and constants
	private static final long serialVersionUID = 6879860210379903374L;
	UnfoldingMap metro;
	float x = 0;
	float y = 0;
	List<Train> trainManager = new ArrayList<Train>();
	List<Feature> allLoFL = Collections.synchronizedList(new ArrayList<Feature>()); //contains all the Features in a List
	List<MultiFeature> newlyadded = new ArrayList<MultiFeature>(); //contains the features that we will add to the current metro map
	Random rand = new Random(100);
	int year = 1980;
	int finalyear = 0;
	int yearcounter = 0;
	String completed = "Subway construction completed";
	String yearStr = Integer.toString(year);
	String name = "Testing";
	boolean modifying = false;
	JFrame infoPanel = new JFrame();
	/**
	 * List of shape features.
	 * Also known as a list of lines.
	 */
	List<ShapeFeature> loSF;
	List<Location> loStations = new ArrayList<Location>();
	List<SimplePointMarker> loSPM = new ArrayList<SimplePointMarker>();
	
	ArrayList<GitInspectorEntry> gie;
	
	public MapPlot(List<ShapeFeature> loSF) {
		this.loSF = loSF;
	}
	
	public MapPlot() {
		
	}
	
	/**
	 * Setting up the visualizer
	 */
	public void setup() {
		size(1280, 720); //setup size of applet, using OpenGL's PGraphics2D as the renderer
		smooth(); //anti-aliase edges
		//frameRate(30f);
		noStroke();	
		Ani.init(this); //initialize Ani, our train moving visualizer...	
		Ani.setDefaultEasing(Ani.QUAD_IN_OUT);
		metro = new UnfoldingMap(this, new Google.GoogleSimplifiedProvider()); //TODO using GoogleSimplified2Provider for now...find a better map style
		
		CodeMetro codemetro = new CodeMetro();
		codemetro.run();
		gie = codemetro.fuser.getGieList();
		//testmethod();
		//noLoop();
		//    /Users/tonychu/Documents/410workspace/CodeMetro/
		loSF = codemetro.getFeatures();
		loSPM = codemetro.getStationMarkers();
		System.out.println("LIST OF MAP FEATURES. Size " + loSF.size());
		for (int i = 0; i < loSF.size(); i++) {
			if (true) break;
				ShapeFeature sf = loSF.get(i);
				System.out.println();
				System.out.println(sf.getProperty("caller"));
				System.out.println(sf.getProperty("callee"));
				for (int k = 0; k < sf.getLocations().size(); k++) {
					System.out.println(sf.getLocations().get(k));
				}
				System.out.println();
		}
		System.out.println("Done iterating.");
		/*
		for (int i = 0; i < loMF.size(); i++) {
			addLine(loMF.get(i));
		}
		*/
		
		

		
		// Add mouse and keyboard interactions 
		//TODO Remove in the future, we don't want users to fiddle with the map.
        MapUtils.createDefaultEventDispatcher(this, metro);
		
		//Show particular location in world map
		Location testMap = new Location(0f,0f);
		int zoomLv = 6; //the level of zoom for the map (0 = world view, bigger number = more zoom) use 12
		metro.zoomAndPanTo(zoomLv, testMap);
		List<Feature> loFL = allLoFL;
		
		List<Feature> templist = new ArrayList<Feature>();

		for (int i = 0; i < loSF.size(); i++) {
			ShapeFeature sf = loSF.get(i);
			
			System.err.println();
			System.err.println(sf.getProperty("edge"));
			CallGraphNode caller = ((CallGraphNode) sf.getProperty("caller"));
			CallGraphNode callee = ((CallGraphNode) sf.getProperty("callee"));
			System.err.println("Caller: " + caller);
			System.err.println("Callee: " + callee);
			System.err.println(sf.getLocations());
			System.err.println();
			
			templist.add(sf); //add all the features inside a multifeature into templist
				
			//linetoStation(templist); //plot the stations
			sf = lineToSubwayLine(sf); //converts lines to Subway-style lines
			//loSF.set(i, sf);
			
			
		}
		System.err.println("I AM PLOTTING LINES.");
		plotLines(metro, loSF); 
		
		for (ShapeFeature sf : loSF) {
			System.err.println("Adding train.");
			addTrain(sf);
		}
		
		System.out.println("Adding Stations");
		plotPoints(metro, loSPM);
		
		/*
		while (newlyadded.size() > 0) {

			if (newlyadded.size() > 0) { //adds the newly added line to the map
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
		}
		*/
		System.err.println("I AM RUNNING TRAINS NOW.");
		run(loFL); //runs the trains across the lines and counts the year
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
					float wPX = wP.getLocations().get(j).x;
					float wPY = wP.getLocations().get(j).y;
					
					for (int k = 0; k < loStations.size(); k++) {
						float stationX = loStations.get(k).x;
						float stationY = loStations.get(k).y;
						if (wPX == stationX && wPY == stationY) {
							plotPoint(metro, wP.getLocations().get(j), 6);
						}
					}
					
					
					loStations.add(wP.getLocations().get(j));
					plotPoint(metro, wP.getLocations().get(j), 3);
					System.out.println();
				}
			}
		}
	}
	
	/**
	 * 
	 * @param wayPoints
	 * @param loF
	 */
	private void addTrain(ShapeFeature sF) { //puts a train onto that line
		//plotPoints(metro, loF); //plots the list of feature markers (loF) onto Unfolding Map(metro)
		for (int i = 0; i < sF.locations.size(); i++) {
			Train train = new Train(sF); //initialize train at beginning of the line
			train.getTrain().setColor(color(0,0,0));
			trainManager.add(train); //add train to trainManager
			metro.addMarker(train.getTrain()); //add train to the map
		}
	}
	
	private synchronized void run(List<Feature> allLoFL) {
		Timer timer = new Timer();
		timer.schedule(new VariableTimerTask(trainManager) {
			@Override
			public synchronized void run() {

				moveTrains();
				
//				yearcounter++;
//				if (yearcounter == 2) {
//					year++;
//					yearcounter = 0;
//				}
//				yearStr = Integer.toString(year);
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
	
	public void addLines(List<ShapeFeature> loSF) {
		this.loSF = loSF;
	}
	
	/**
	 * Converts List<Features> to MultiFeature object containing each Feature.
	 * @param loF list of features
	 * @return MultiFeature class
	 */
	private MultiFeature listToMultiFeat(List<ShapeFeature> loF) { //Converts List<Feature> into MultiFeature
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
	/*
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
	*/
	/**
	 * Plot the point with the given parameters
	 * @param metro UnfoldingMap class
	 * @param bigger 
	 * @param feat a feature
	 * @return void 
	 */
	private void plotPoint(UnfoldingMap metro, Location loc, int markerSize) {
		//Plot Points
		SimplePointMarker simpleMarker;
		simpleMarker = new SimplePointMarker(loc);
		simpleMarker.setStrokeWeight(markerSize);
		simpleMarker.setStrokeColor(color(0,0,0)); //Black
		

		metro.addMarker(simpleMarker);
	}
	
	private void plotPoints(UnfoldingMap metro, List<SimplePointMarker> loSPM) {
		for (int i = 0; i < loSPM.size(); i++) {
			SimplePointMarker simpleMarker = loSPM.get(i);
			loSPM.get(i).setStrokeWeight(3);
			loSPM.get(i).setStrokeColor(color(0,0,0));
			
			// Colour scaling.
			CallGraphNode node = (CallGraphNode) simpleMarker.getProperty("data");
			
			
			// Colour scaling.
			int innerColor = color(255,255,255);
			int incomingCnt = node.incoming.size();
			int outgoingCnt = node.outgoing.size();
			if ((incomingCnt + outgoingCnt) > 0){
				double ratio = outgoingCnt / (double) (outgoingCnt + incomingCnt);
				int outPart = (int) (ratio * 255);
				int scaledOut = (int) (ratio * 64 + 192);
				int scaledIn = (int) (ratio * -64 + 192);
				innerColor = color(255 - outPart, 255, 0);
			} 
			
			simpleMarker.setColor(innerColor); //White
			
			metro.addMarker(loSPM.get(i));
		}
	}
	
	/**
	 * Takes a List of ShapeFeatures and plots them onto the the metro.
	 * Each Feature should be a single line.
	 * @param metro
	 * @param loFL
	 * @return
	 */
	public MultiFeature plotLines(UnfoldingMap metro, List<ShapeFeature> loFL) {
		//Plot Lines
		MultiFeature mF = listToMultiFeat(loFL);
		
		return plotLines(metro, mF);
	}
	
	/**
	 * Takes a MultiFeature containing multiple Features and plots them onto the metro.
	 * Each Feature should be a line.
	 * @param metro
	 * @param mF
	 * 
	 */
	public MultiFeature plotLines(UnfoldingMap metro, MultiFeature mF){
		System.err.println("Plotting each shape in the multifeature.");
		for (int index = 0; index < mF.getFeatures().size(); index++) { //for each polyline set
			ShapeFeature line = (ShapeFeature) mF.getFeatures().get(index);
			//Plot lines onto map and add characteristics to lines
			SimpleLinesMarker lineMarker = new SimpleLinesMarker(line.getLocations());
			lineMarker.setStrokeWeight(5);
			lineMarker.setColor(color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255))); //just some random color

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
					System.out.println("DIDN'T CHANGE A POINT.");
				}
			}
			
		}
		return linePoint;
	}
	
	private synchronized void moveTrains(){
		noLoop();
		for (int i = 0; i < trainManager.size(); i++) { //moves each train to the next waypoint
			Train train = trainManager.get(i);
			int result = moveTrain(train);
			if (result == -1) { //TODO
				
			}
		}
		loop();
	}
	
	private synchronized int moveTrain(Train train) {
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
	 * Finds the station under mouse position and pops and overlay
	 */
	public synchronized void mousePressed() {
		noLoop();
		float currentmouseX = mouseX;
		float currentmouseY = mouseY;
		Location currentmouseLoc = metro.getLocation(currentmouseX, currentmouseY);
		System.out.println("Finding a station");
		float epsilon = 0.3f;
		for (int i = 0; i < loSPM.size(); i++) {
			SimplePointMarker spm = loSPM.get(i);
			float currentStationX = spm.getLocation().x;
			float currentStationY = spm.getLocation().y;
			if ((Math.abs(currentStationX - currentmouseLoc.x) < epsilon) && (Math.abs(currentStationY - currentmouseLoc.y) < epsilon)) {
				System.out.println("Found a station!");
				
				//infoPanel.dispatchEvent(new WindowEvent(infoPanel, WindowEvent.WINDOW_CLOSING));
				infoPanel = new JFrame();
				infoPanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				infoPanel.setBounds(300, 200, 800, 600);
				infoPanel.setVisible(true);
				
				infoPanel.setTitle("Station name: " +  spm.getId());
				System.out.println("Authors: " + spm.getProperty("authors"));
				CallGraphNode cgn = (CallGraphNode) spm.getProperty("data");
				System.out.println(cgn.toString());
				
				Object outData[][] = new Object[cgn.outgoing.size()][2];
				for (int j = 0; j < cgn.outgoing.size(); j++) {
					CallGraphEdge edge = cgn.outgoing.get(j);
					outData[j][0] = edge.getQualifiedCaller();
					outData[j][1] = edge.getQualifiedCallee();
				}
				
				Object rowData[][] = new Object[cgn.incoming.size()][2];
				for (int j = 0; j < cgn.incoming.size(); j++) {
					CallGraphEdge edge = cgn.incoming.get(j);
					rowData[j][0] = edge.getQualifiedCaller();
					rowData[j][1] = edge.getQualifiedCallee();
				}
				
				
				//Object rowData[][] = {	{spm.getProperty("authors"), spm.getProperty("authors")},
				//					 };
								
				
				
				JPanel authorPanel = new JPanel();
				HashMap<String, Integer> authors = (HashMap<String, Integer>) spm.getProperty("authors");
				if (authors.size() == 0) {
					//TODO no author found
					authorPanel.add(new JLabel("No author data found."));
				} else {

					Object authorRow[][] = new Object[authors.size()][2];

					Set<String> aKeys = authors.keySet();
					Iterator<String> aIter = (Iterator<String>) aKeys.iterator();
					while (aIter.hasNext()){
						String aName = aIter.next();
						int j = 0;
						authorRow[j][0] = aName;
						authorRow[j][1] = authors.get(aName);
						j++;
					}
					Object authorColumnNames[] = {"Author", "Lines Modified"};
					JTable authortable = new JTable(authorRow, authorColumnNames);
					authorPanel.add(authortable);
				}

				Object outColumnNames[] = {"Calling Method", "Called Method"};

				JTable outCallTable = new JTable(outData, outColumnNames);
				JScrollPane outPane = new JScrollPane(outCallTable);
				
				Object inColumnNames[] = {"Calling Method", "Called Method"};

				JTable inCallTable = new JTable(rowData, inColumnNames);
				JScrollPane inPane = new JScrollPane(inCallTable);

				JPanel contentPane = new JPanel();
				contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
				contentPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

				infoPanel.add(contentPane);
				contentPane.add(authorPanel);
				contentPane.add(outPane);
				contentPane.add(inPane);

				//infoPanel.pack();
				break;
			}
		}
		loop();
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
			train.getMarker().setLocation(train.getLocation());
			train.getMarker().setLocation(train.getX(), train.getY());
		}
	}
}
