package codemetro;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JApplet;
import javax.swing.JFrame;

import processing.core.PApplet;
import codemetro.analyzer.callgraph.CallGraphNode;
import codemetro.analyzer.callgraph.CallGraphRunner;
import codemetro.analyzer.callgraph.parser.CallGraphParser;
import codemetro.analyzer.gitinspector.GitInspector;
import codemetro.analyzer.gitinspector.GitInspectorEntry;
import codemetro.fuser.Fuser;
import codemetro.visualizer.MapPlot;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.MultiFeature;

public class CodeMetro{

	List<MultiFeature> feat;
	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
		CodeMetro inst = new CodeMetro();
		inst.run();
		if (inst.feat != null){
			MapPlot m = new MapPlot();
			JFrame frame = new JFrame();
			frame.add(m, BorderLayout.CENTER);
			m.init();
			m.setup();
		}
	}
	
	public void run(){
		//TODO Call both call graph and gitinspector.

		System.out.println("Please input the path to the directory of the repository.");
		Scanner scan = new Scanner(System.in);

		String repoRoot = scan.nextLine();

		scan.close();
		File root = new File(repoRoot);
		if (!root.exists()){
			System.err.println("Path to repository does not exist.");
		} else {
			Map <String, CallGraphNode> callGraph;
			CallGraphParser cg = new CallGraphParser();
			try {
				callGraph = cg.generateGraph(repoRoot);


				// Inspect our code and parse into an ArrayList
				GitInspector gi1 = new GitInspector();
				gi1.inspect(repoRoot);
				ArrayList<GitInspectorEntry> c1 = gi1.parseOutput();
				// Inspect other code and parse into an ArrayList

				//TODO Fuse both metrics into station/train.
				Fuser f = new Fuser(c1, callGraph);
				feat = f.createTransit();
				//TODO convert into visualizer input
				//TODO visualize
				//System.out.println(feat);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<MultiFeature> getFeatures(){
		return feat;
	}
}
