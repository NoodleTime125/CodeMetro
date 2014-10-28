package codemetro.analyzer.callgraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CallGraphRunner {
	
	public static String callStrRe = "^M:(.*):(.*) \\([OMSI]\\)(.*):(.*)$";	
	
	public Map<String, CallGraphNode> generateGraph(String target) throws IOException {
		List<String> output = run(target);
		return parseOutput(output);
	}
	
	protected List<String> run(String target) throws IOException {
		String loc = "lib/java-callgraph-static.jar";

		ProcessBuilder pb = new ProcessBuilder("java", "-jar", new File(loc).getPath(), new File(target).getCanonicalPath());
		pb.redirectErrorStream(true);
		Process p = pb.start();
		
		String line = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		
		List<String> output = new ArrayList<String>();
		while ((line = in.readLine()) != null) {
            output.add(line);
		}
		return output;
	}

	/**
	 * Parses a textual callgraph from java-callgraph to generate an internal callgraph.
	 * @param output
	 * @return a callgraph
	 */
	protected Map<String, CallGraphNode> parseOutput(List<String> output) {
		Map <String, CallGraphNode> nodeList = new HashMap<String, CallGraphNode>();
		for (String line : output){
			if(line.matches(callStrRe)){
				Matcher m = Pattern.compile(callStrRe).matcher(line);
				m.find();
				
				CallGraphNode outNode;
				CallGraphNode inNode;
				
				if (nodeList.containsKey(m.group(1))){
					outNode = nodeList.get(m.group(1));
				} else {
					outNode = new CallGraphNode(m.group(1));
					nodeList.put(m.group(1), outNode);
				}
				
				if (nodeList.containsKey(m.group(3))){
					inNode = nodeList.get(m.group(3));
				} else {
					inNode = new CallGraphNode(m.group(3));
					nodeList.put(m.group(3), inNode);
				}
				
				CallGraphEdge edge = new CallGraphEdge(m.group(2), m.group(4));
				edge.addEdgeBetween(outNode, inNode);
				
				System.out.println(m.group(2) + " in " + m.group(1) + " calls method " + m.group(4) + " in " + m.group(3));
			}
		}
		return nodeList;
	}
}
