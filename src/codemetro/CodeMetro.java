package codemetro;

import java.io.IOException;
import java.util.ArrayList;

import codemetro.analyzer.callgraph.CallGraphRunner;
import codemetro.analyzer.gitinspector.GitInspector;
import codemetro.analyzer.gitinspector.GitInspectorEntry;

public class CodeMetro {

	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
		//TODO Call both call graph and gitinspector.
		CallGraphRunner cg = new CallGraphRunner();
		try {
			cg.generateGraph("lib/core.jar");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Inspect our code and parse into an ArrayList
		GitInspector gi1 = new GitInspector();
		gi1.inspect(".");
		ArrayList<GitInspectorEntry> c1 = gi1.parseOutput();
		// Inspect other code and parse into an ArrayList
//		GitInspector gi2 = new GitInspector();
//		gi2.inspect("lib/codebase/Halja");
//		ArrayList<GitInspectorEntry> c2 = gi2.parseOutput();
		
		//TODO Fuse both metrics into station/train.
		//TODO convert into visualizer input
		//TODO visualize
	}

}
