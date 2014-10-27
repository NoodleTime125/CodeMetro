package codemetro.analyzer.callgraph;

/**
 * This represents a directed edge in a call graph.
 * @author Kenneth Shen
 *
 */
public class CallGraphEdge {
	
	private CallGraphNode from;
	private CallGraphNode to;
	private String caller;
	private String callee;
	
	public CallGraphEdge (String caller, String callee){
		this.caller = caller;
		this.callee = callee;
	}
	/**
	 * Adds this edge between two CallGraphNodes
	 * @param from
	 * @param to
	 */
	public void addEdgeBetween(CallGraphNode from, CallGraphNode to){
		this.from = from;
		this.to = to;
		from.addOutgoingEdge(this);
		to.addIncomingEdge(this);
	}
	
	
	@Override
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
}