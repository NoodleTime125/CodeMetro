package codemetro.analyzer.gitinspector;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Map.Entry;

public class GitInspectorEntry {
	// Instant fields and constants
	private String name;
	private HashMap<String, Integer> hm = new HashMap<String, Integer>();

	/**
	 * Constructor
	 * @param name name of the entry
	 */
	public GitInspectorEntry(String name) {
		this.name = name;
	}
	/**
	 * @param i index
	 * @param s file entry
	 */
	public void fileEntry(Integer i, String s) {
		//System.out.println("start file entry");
		hm.put(s, i);
		//System.out.println("added file entry");
	}
	@Override
	/**
	 * Iterate through a hashmap and return
	 * @return String interated hashmap with name
	 */
	public String toString(){
		return name + "\n" + IterateHashMap();
	}
	/**
	 * @return String a list of entries
	 */
	public String IterateHashMap() {
	    Iterator<Entry<String, Integer>> it = hm.entrySet().iterator();
	    StringBuilder buff = new StringBuilder();
	    while (it.hasNext()) {
	        Map.Entry<String, Integer> pairs = (Entry<String, Integer>)it.next();
	        buff.append(pairs.getKey() + " " + pairs.getValue()+ "\n");
	    }
	    return buff.toString();
	}
	/**
	 * @return HashMap a list of <String, Integer>
	 */
	public HashMap<String, Integer> getHashMap() {
		return hm;
	}
	/**
	 * @return String name
	 */
	public String getName() {
		return name;
	}

}
