package codemetro.timeline;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import codemetro.analyzer.callgraph.CallGraphEdge;
import codemetro.analyzer.callgraph.CallGraphNode;
import codemetro.analyzer.callgraph.CallGraphRunner;

/**
 * Represents a call graph at a commit.
 * @author Kenneth Shen
 *
 */
public class TimeSlice {
	// Instant fields and constants
	public String commitId;
	/** 
	 * Target to run callgraph on.
	 */
	public static String target;
	public TimeSlice next;
	public TimeSlice prev;
	public Map<String, CallGraphNode> nodes;
	public List<CallGraphEdge> edges;
	
	public TimeSlice(String commitId){
		this.commitId = commitId;
	}
	
	public static void setTarget(String newTarget){
		target = newTarget;
	}
	
	/**
	 * Generates a single graph for a time slice.
	 * @throws IOException 
	 */
	public void generateGraph() throws IOException{
		CallGraphRunner inst = new CallGraphRunner();
		nodes = inst.generateGraph(target);
	}
}
