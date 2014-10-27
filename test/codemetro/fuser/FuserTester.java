package codemetro.fuser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import codemetro.fuser.Fuser;

public class FuserTester {
	public static void main(String args[]){
		//Fuser f = new Fuser();
		
		/*
		ArrayList<Station> sList = new ArrayList<Station>();
		sList.add(new Station("testing"));
		
		if(sList.contains(new Station("testing"))){
			System.out.println("found");
		} else
			System.out.println("not found");
		*/
		
		HashMap hm = new HashMap();
		if(hm.isEmpty()) System.out.println("hashmap empty");
		hm.put(new Station("some path"), "some path");
		if(!hm.isEmpty()) System.out.println("hashmap not empty");
		
		if(hm.containsValue("some path")){
			System.out.println("found");
		} else
			System.out.println("not found");
		/*
		 // Get a set of the entries
	      Set set = hm.entrySet();
	      // Get an iterator
	      Iterator i = set.iterator();
	      // Display elements
	      while(i.hasNext()) {
	         Map.Entry me = (Map.Entry)i.next();
	         System.out.print(((Station) me.getKey()).getName() + ": ");
	         System.out.println(me.getValue());
	      }
	      */
	}
}
