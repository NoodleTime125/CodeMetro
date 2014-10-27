package codemetro.fuser;

import java.util.ArrayList;

public class Station {
	public Station(int size, String name){
		this.stationSize = size;
		this.stationName = name;
		this.inTrain= new ArrayList<Train>();
		this.outTrain = new ArrayList<Train>();		
	}
	
	public void changeSize(int size){	
		this.stationSize = size;
	}
	
	public void changeName(String name){
		this.stationName = name;
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
	
	private int stationSize;			// line count per class
	private String stationName;			// n-th commit
	private ArrayList<Train> inTrain; 	// input parameter
	private ArrayList<Train> outTrain;	// output parameter
}
