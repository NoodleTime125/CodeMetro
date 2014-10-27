package codemetro.analyzer.gitinspector;

import java.util.HashMap;
import java.util.Iterator;

public class GitInspectorEntry {
	
	private String name;
	private HashMap<String, Integer> hm = new HashMap<String, Integer>();

	public GitInspectorEntry(String name) {
		this.name = name;
	}
	
	public void fileEntry(Integer i, String s) {
		System.out.println("start file entry");
		hm.put(s, i);
		System.out.println("added file entry");
	}
	
	@Override
	public String toString(){
		return name + "\n" + printMap();
	}
	
	public String printMap() {
	    Iterator it = hm.entrySet().iterator();
	    StringBuilder buff = new StringBuilder();
	    while (it.hasNext()) {
	        HashMap.Entry pairs = (HashMap.Entry)it.next();
	        buff.append(pairs.getKey() + " = " + pairs.getValue()+ "\n");
	    }
	    return buff.toString();
	}

}
