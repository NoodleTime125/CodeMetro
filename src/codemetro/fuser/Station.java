package codemetro.fuser;

import java.util.ArrayList;

public class Station {
	
	private int stationSize;			// line count per class
	private String stationName;			// path name  
	private ArrayList<Train> inTrain; 	// input parameter
	private ArrayList<Train> outTrain;	// output parameter
	private ArrayList<String> aList;	// author list
	
	public Station(String name){
		this.stationName = name;
		this.aList = new ArrayList<String>();
		this.inTrain = new ArrayList<Train>();
		this.outTrain = new ArrayList<Train>();		
	}
	
	public void setSize(int size){	
		this.stationSize = size;
	}
	
	public void setName(String name){
		this.stationName = name;
	}
	
	/*
	 * @param name name of the author
	 * @param nLines number of lines the author has contributed
	 */
	public void addAuthor(String name, int nLines){
		this.aList.add(name +" "+ nLines);
	}
	
	public void addTrainCart(String s, Object obj){
		if(s.equals("in"))	// inTrain
			inTrain.add(new Train(obj));
		else				// outTrain
			outTrain.add(new Train(obj));
	}
	
	public void removeTrainCart(ArrayList<Train> train, int index){
		train.remove(index);
	}
	
	public int getSize(){
		return this.stationSize;
	}
	
	public String getName(){
		return this.stationName;
	}
	

}
