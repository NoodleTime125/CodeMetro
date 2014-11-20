package codemetro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import codemetro.analyzer.callgraph.CallGraphNode;
import codemetro.analyzer.callgraph.CallGraphRunner;
import codemetro.analyzer.callgraph.parser.CallGraphParser;
import codemetro.analyzer.gitinspector.GitInspector;
import codemetro.analyzer.gitinspector.GitInspectorEntry;
import codemetro.fuser.Fuser;

public class CodeMetro {

	
	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
		//TODO Call both call graph and gitinspector.
		String repoRoot = ".";
		Map <String, CallGraphNode> callGraph;

		CallGraphParser cg = new CallGraphParser();
		try {
			callGraph = cg.generateGraph(repoRoot);
		
		
		// Inspect our code and parse into an ArrayList
		GitInspector gi1 = new GitInspector();
		gi1.inspect(repoRoot);
		ArrayList<GitInspectorEntry> c1 = gi1.parseOutput();
		// Inspect other code and parse into an ArrayList
//		GitInspector gi2 = new GitInspector();
//		gi2.inspect("lib/codebase/Halja");
//		ArrayList<GitInspectorEntry> c2 = gi2.parseOutput();
		
		//TODO Fuse both metrics into station/train.
		new Fuser(c1, callGraph);
		//TODO convert into visualizer input
		//TODO visualize
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
