package codemetro;

import java.io.IOException;

import codemetro.analyzer.callgraph.CallGraphRunner;

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
		//TODO Fuse both metrics into station/train.
		//TODO convert into visualizer input
		//TODO visualize
	}

}
