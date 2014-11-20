package codemetro.analyzer.callgraph;

/**
 * This represents a directed edge in a call graph.
 * @author Kenneth Shen
 */
public class CallGraphEdge {
	
	private CallGraphNode from;
	private CallGraphNode to;
	private String caller;
	private String callee;
	
	/**
	 * Constructor 
	 * @param caller		
	 * @param callee
	 */
	public CallGraphEdge (String caller, String callee){
		this.caller = caller;
		this.callee = callee;
	}
	/**
	 * Adds an edge between two CallGraphNodes
	 * @param from 	a CallGraphNode
	 * @param to	a CallGraphNode
	 */
	public void addEdgeBetween(CallGraphNode from, CallGraphNode to){
		this.from = from;
		this.to = to;
		from.addOutgoingEdge(this);
		to.addIncomingEdge(this);
	}
	
	@Override
	/**
	 * Checks if an object is a CallGraphEdge or a String
	 * @param o 		an object to be checked
	 * @return boolen 	true if an object is a Callgraph or String
	 */
	public boolean equals(Object o){
		if (!(o instanceof CallGraphEdge)){
			return false;
		}
		CallGraphEdge other = (CallGraphEdge) o;
		if (from != other.from || to != other.to){
			return false;
		}
		if (caller != other.caller || callee != other.callee){
			return false;
		}
		return true;
	}
	
	public String getCaller(){
		return caller;
	}
	
	public String getCallee(){
		return callee;
	}
	
	public CallGraphNode getCallerMethod(){
		return from;
	}
	
	public CallGraphNode getCalleeMethod(){
		return to;
	}
}