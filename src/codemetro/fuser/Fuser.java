package codemetro.fuser;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.*;

import org.json.JSONException;
import org.json.JSONWriter;

public class Fuser {
	public Fuser(){
		PrintWriter pw;
		try {
			pw = new PrintWriter("test.txt");
			new JSONWriter(pw)
		     .object()
		         .key("type").value("FeatureCollection")
		         	.key("features").array()
		         		.object()
		         			.key("type").value("Feature")
		         			.key("geometry").object()
		         				.key("type").value("LineString")
		         				.key("coordinates").array()
		         					.array().value(-123.1).value(49.240).endArray()
		         					.array().value(-123.13).value(49.34).endArray()
		         					.array().value(-123.13).value(49.34).endArray()
		         					.array().value(-123.13).value(49.240).endArray()
		         				.endArray()
		         			.endObject()	
		         		.endObject()			
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

/*
		obj = new JSONObject();
		obj.put("type" , "FeatureCollection");
		
		JSONArray coordinates = new JSONArray();
		coordinates.add("-123.1,49.240");
		coordinates.add("-123.13,49.34");
		coordinates.add("-123.13,49.34");
		coordinates.add("-123.13,49.240");
		
		JSONArray coorList = new JSONArray();
		coorList.add(coordinates);
		
		JSONArray featureList = new JSONArray();
		featureList.add(new JSONObject().put("type","Feature"));
		featureList.add(new JSONObject().put("geometry",new JSONObject().put("type","LineString")));
		featureList.add(coorList);
		
		
		obj.put("features", featureList);
		
		
		//try {
			 
			FileWriter file = new FileWriter("Line.json");
			file.write(obj.toJSONString());
			file.flush();
			file.close();
		

	 
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(obj.toJSONString());
		
		
*/