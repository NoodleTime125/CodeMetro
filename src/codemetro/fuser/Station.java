package codemetro.fuser;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import codemetro.analyzer.callgraph.CallGraphNode;
import codemetro.analyzer.gitinspector.GitInspectorEntry;
import de.fhpotsdam.unfolding.geo.Location;

public class Station {
	private String stationName;			// class and path name
	private ArrayList<Train> inTrain = new ArrayList<Train>(); 	// input parameter
	private ArrayList<Train> outTrain = new ArrayList<Train>();	// output parameter
	private CallGraphNode cgn;			// CallGraphNode
	private Location coordinate;
	private double xCoordinate;
	private double yCoordinate;
	private HashMap<String, Integer> aList = new HashMap<String, Integer>();;	// author list
	
	public Station(String name){
		this.stationName = name;
	}
	
	public void setCallGraphNode(CallGraphNode cgn){
		this.cgn = cgn;
	}
	public CallGraphNode getCallGraphNode(){
		return cgn;
	}
	public void setName(String name){
		this.stationName = name;
	}
	
	/*
	public void setCoordinate(double x, double y){
		this.coordinate.setLocation(x,y);
	}
	public Point getCoordinate(){
		return coordinate;
	}
	*/
	
	public void setLocation(Location loc){
		coordinate = loc;
	}
	
	public Location getLocation(){
		return coordinate;
	}
	
	
	
	/**
	 * @param name name of the author
	 * @param nLines number of lines the author has contributed
	 */
	public void addAuthor(String name, int nLines){
		aList.put(name, nLines);
	}
	
	public HashMap<String, Integer> getAuthors(){
		return aList;
	}
	/**
	 * adds a train to an array list of trains
	 * @param s name of train
	 * @param obj object
	 */
	public void addTrainCart(String s, Object obj){
		if(s.equals("in"))	// inTrain
			inTrain.add(new Train(obj));
		else				// outTrain
			outTrain.add(new Train(obj));
	}
	
	/**
	 * removes a train from an arraylist of trains
	 * @param train list of trains
	 * @param index index
	 */
	public void removeTrainCart(ArrayList<Train> train, int index){
		train.remove(index);
	}
	
	/**
	 * @return String name
	 */
	public String getName(){
		return this.stationName;
	}
	
	public String toString(){
		return stationName + aList + cgn;
	}

}
