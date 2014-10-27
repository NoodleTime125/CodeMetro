package codemetro.fuser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONStringer;
import org.json.JSONWriter;

import codemetro.analyzer.gitinspector.GitInspectorEntry;

public class Fuser {
	private ArrayList<GitInspectorEntry> gieList;
	private HashMap sList;
	//private ArrayList<Station> sList;
	
	public Fuser(){
		// do nothing ; for testing purposes
	}
	
	public Fuser(ArrayList<GitInspectorEntry> gieList){
		this.gieList = gieList;
		//this.sList = new ArrayList<Station>();
		this.sList = new HashMap();
		
		for(GitInspectorEntry gie : gieList){		// iterate through gie set
			String name = gie.getName();
			HashMap hm = gie.getHashMap();

		    Set set = hm.entrySet(); 				// get hashmap set
		    Iterator i = set.iterator();			// iterator
		    while(i.hasNext()) {					// get elements
		    	Map.Entry me = (Map.Entry)i.next();
		        //System.out.print(me.getKey() + ": ");
		        //System.out.println(me.getValue());
			
			    if(!sList.isEmpty()){ //check if empty
			    	if(sList.containsValue(me.getValue()+"")){
			    		// TO DO
			    		// do not duplicate if already in the list
			    	} else {
			    		// put Station and class path in the list
			    		sList.put(new Station(me.getKey()+""),me.getKey()+""); 
			    	}
		    	}
		    }
		}
		
		
	}
	
	public void createMarker(String path, int nLines){
		PrintWriter pw;
		try {
			pw = new PrintWriter("Marker.json");
			new JSONWriter(pw)
		     .object()
		         .key("type").value("FeatureCollection")
		         	.key("features").array()
		         		// loop
		         		.object()
		         			.key("type").value("Feature")
		         			.key("geometry").object()
		         				.key("type").value("Point")
		         				.key("coordinates").array()
		         					.value(-123.1).value(49.240)
		         				.endArray()
		         			.endObject()
		         			.key("properties").object()
		         				.key("path").value("nLines")
		         			.endObject()
		         		.endObject()
		         		// end loop
		         	.endArray()
		     .endObject();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e){
			System.out.println("JSONException happened!");
			e.printStackTrace();
		} 
	}
	public void createLine(String path, int nLines){
		PrintWriter pw;
		try {
			pw = new PrintWriter("Line.json");
			new JSONWriter(pw)
		     .object()
		         .key("type").value("FeatureCollection")
		         	.key("features").array()
		         		//loop
		         		.object()
		         			.key("type").value("Feature")
		         			.key("geometry").object()
		         				.key("type").value("LineString")
		         				.key("coordinates").array()
		         					.array().value(0).value(0).endArray()
		         					.array().value(1).value(3).endArray()
		         				.endArray()
		         			.endObject()	
		         		.endObject()
		         		// end loop
		         	.endArray()
		     .endObject();
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e){
			System.out.println("JSONException happened!");
			e.printStackTrace();
		} 
	}

	
}