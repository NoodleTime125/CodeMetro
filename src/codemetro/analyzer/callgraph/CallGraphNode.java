package codemetro.analyzer.callgraph;

import java.util.ArrayList;

public class CallGraphNode {
	
	public String className;
	public ArrayList<CallGraphEdge> outgoing = new ArrayList<CallGraphEdge>();
	public ArrayList<CallGraphEdge> incoming = new ArrayList<CallGraphEdge>();
	public CallGraphNode(String className){
		this.className = className;
	}
	
	public void addIncomingEdge(CallGraphEdge e){
		if (!incoming.contains(e)){
			incoming.add(e);
		}
	}
	
	public void addOutgoingEdge(CallGraphEdge e){
		if (!outgoing.contains(e)){
			outgoing.add(e);
		}
	}
	
	public boolean equals(Object o){
		if (!(o instanceof CallGraphNode)){
			return false;
		}
		CallGraphNode other = (CallGraphNode) o;
		if (className.equals(other.className)){
			return true;
		}
		return false;
	}
}
