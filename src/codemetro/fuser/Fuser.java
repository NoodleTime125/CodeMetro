package codemetro.fuser;
import java.util.*;
import java.text.*;

public class Fuser {
	public Fuser(Object inputAT, Object inputGI){
		this.stationName = "TODO";	
		this.year = 0000; 			
		this.inTrain= new ArrayList<Train>();
		this.outTrain = new ArrayList<Train>();
		this.stationSize = 0000;
		
		System.out.println(getDate().toString());
	}
	public void addCarriage(String s, Train t){
		if(s.equals("in"))	// inTrain
			inTrain.add(t);
		else				// outTrain
			outTrain.add(t);
	}
	public Date getDate(){
		return new Date();
	}
	private int year;					// class name
	private String stationName;			// n-th commit
	private int stationSize;			// line count per class
	private ArrayList<Train> inTrain; 	// input parameter
	private ArrayList<Train> outTrain;	// output parameter
	private Object input1;
	private Object input2;

}