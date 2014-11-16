package codemetro.fuser;

import java.util.ArrayList;

public class Station {
	private String stationName;			// class and path name
	private ArrayList<Train> inTrain; 	// input parameter
	private ArrayList<Train> outTrain;	// output parameter
	private ArrayList<String> aList;	// author list
	
	public Station(String name){
		this.stationName = name;
		this.aList = new ArrayList<String>();
		this.inTrain = new ArrayList<Train>();
		this.outTrain = new ArrayList<Train>();		
	}
	
	public Station(String name, String author){
		this.stationName = name;
		this.aList = new ArrayList<String>();
		aList.add(author);
		this.inTrain = new ArrayList<Train>();
		this.outTrain = new ArrayList<Train>();		
	}
	
	public void setName(String name){
		this.stationName = name;
	}
	
	/**
	 * @param name name of the author
	 * @param nLines number of lines the author has contributed
	 */
	public void addAuthor(String name, int nLines){
		this.aList.add(name +" "+ nLines);
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
	

}
