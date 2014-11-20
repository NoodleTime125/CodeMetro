package codemetro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import codemetro.analyzer.callgraph.CallGraphNode;
import codemetro.analyzer.callgraph.CallGraphRunner;
import codemetro.analyzer.callgraph.parser.CallGraphParser;
import codemetro.analyzer.gitinspector.GitInspector;
import codemetro.analyzer.gitinspector.GitInspectorEntry;
import codemetro.fuser.Fuser;
import de.fhpotsdam.unfolding.data.Feature;

public class CodeMetro {

	
	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
		//TODO Call both call graph and gitinspector.
		System.out.println("Please input the path to the directory of the repository.");
		Scanner scan = new Scanner(System.in);
		
		String repoRoot = scan.nextLine();
		
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
		Fuser f = new Fuser(c1, callGraph);
		List<Feature> feat = f.createTransit();
		//TODO convert into visualizer input
		//TODO visualize
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
