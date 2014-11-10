package codemetro.analyzer.callgraph;

import java.util.ArrayList;

public class CallGraphNode {
	
	public String className;
	public ArrayList<CallGraphEdge> outgoing = new ArrayList<CallGraphEdge>();
	public ArrayList<CallGraphEdge> incoming = new ArrayList<CallGraphEdge>();
	
	/**
	 * Constructor
	 * @param className name of the CallGraphNode
	 */
	public CallGraphNode(String className){
		this.className = className;
	}
	
	/**
	 * 
	 * @param e add CallGraphEdge to the list of incoming CallGraphEdge
	 */
	public void addIncomingEdge(CallGraphEdge e){
		if (!incoming.contains(e)){
			incoming.add(e);
		}
	}
	
	/**
	 * 
	 * @param e add CallGraphEdge to the list of outgoing CallGraphEdge
	 */
	public void addOutgoingEdge(CallGraphEdge e){
		if (!outgoing.contains(e)){
			outgoing.add(e);
		}
	}
	
	/**
	 * check if an object is a CallGraphNode
	 * @return boolean true if an object is a CallGraphNode otherwise false
	 */
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
