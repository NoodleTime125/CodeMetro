package codemetro.fuser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
//import org.json.JSONStringer;
import org.json.JSONWriter;

import codemetro.analyzer.callgraph.CallGraphNode;
import codemetro.analyzer.gitinspector.GitInspectorEntry;

public class Fuser {
	private ArrayList<GitInspectorEntry> gieList;
	private Map<String, CallGraphNode> cgnList;
	private HashMap<String, Station> sList;
	//private HashMap hm;
	//private ArrayList<Station> stationList;
	
	public Fuser(){
		// do nothing ; for testing purposes
	}
	
	public Fuser(ArrayList<GitInspectorEntry> gieList){
		this.gieList = gieList;								// list of GitInspectorEntry ... (Author,<path, #lines>)
		this.cgnList = null;								// list of CallGraphNode ... (
		this.sList = new HashMap<String, Station>();		// <key, value>

		
		stationsMaker();	// GitInspector
		linkStations();		// CallGraph
	}
	
	private void stationsMaker(){
		for(GitInspectorEntry gie : gieList){				// iterate through gie set
			String authorName = gie.getName();				// author's name
			HashMap<String, Integer> hm = gie.getHashMap();	// class and path , # of lines
			
			Set<Map.Entry<String, Integer>> set = hm.entrySet(); 			// get hashmap set
		    Iterator<Entry<String, Integer>> iterator = set.iterator();		// iterator <class and path, lineContributed>

		    while(iterator.hasNext()) {						// get elements
		    	Map.Entry<String, Integer> me = (Map.Entry<String, Integer>)iterator.next(); // String=key, Integer=value
		    	String stationName = me.getKey();			// class and path
		    	Integer lineContributed = me.getValue();
		    	//if(!sList.isEmpty()){						// if station list is not empty
		    		if(sList.containsValue(stationName)){	// if the class and path already exists
		    			sList.get(stationName).addAuthor(authorName, me.getValue());
		    		} else{									// else add the new station
		    			sList.put(stationName, new Station(stationName, authorName+" "+lineContributed));
		    		}
		    	/*} else{									// initialize station list
		    		sList.put(stationName, new Station(stationName,authorName+" "+lineContributed));
		    	}*/
		    }
		}
		System.out.println(sList);
	}
	
	private void linkStations(){ // NOT DONE
		Map<String, CallGraphNode> results = cgnList;
	}

	/**
	 * Creates a coordinate marker for the visualizer
	 * @param hm a hashmap of classes
	 */
	public void createMarker(HashMap<String, Station> hm){ //NOT DONE
		try {
			PrintWriter pw = new PrintWriter("Marker.json");
			JSONWriter jsonW = new JSONWriter(pw);
		    jsonW.object()
		         .key("type").value("FeatureCollection")
		         	.key("features").array();
		         		// loop
		    			Set<Map.Entry<String, Station>> set = hm.entrySet(); 				// get hashmap set
		    			Iterator i = set.iterator();			// iterator
		    			int j = 0;
		    			while(i.hasNext()) {					// get elements
		    				Map.Entry me = (Map.Entry)i.next();
			    			jsonW.object()
			         			.key("type").value("Feature")
			         			.key("geometry").object()
			         				.key("type").value("Point")
			         				.key("coordinates").array()
			         					.value(j).value(j)		// coordinates (will implement spirial rather than diagonal)
			         				.endArray()
			         			.endObject()
			         			.key("properties").object()
			         				.key("path").value(me.getValue())
			         			.endObject()
			         		.endObject();
			    			j+=0.1; 							// this is for coordinates
		    			}
		         		// end loop
		         	jsonW.endArray()
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
	public void createLine(String path, int nLines){ // NOT DONE
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