package codemetro.timeline;

import java.util.List;
import java.util.Map;

import codemetro.analyzer.callgraph.CallGraphEdge;
import codemetro.analyzer.callgraph.CallGraphNode;

public class TimeSlice {
	
	public String commitId;
	public Map<String, CallGraphNode> nodeMap;
	public List<CallGraphEdge> edgeList;
	public TimeSlice(String commitId){
		this.commitId = commitId;
	}
	
	
}
