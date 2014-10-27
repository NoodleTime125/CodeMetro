package codemetro.analyzer.callgraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CallGraphRunner {
	
	public static String callStrRe = "M:(.*) \\([OMSI]\\)(.*)";
	public ArrayList<String> output = new ArrayList<String>();
	public void generateGraph() throws IOException {
		String loc = "lib/java-callgraph-static.jar";
		String target = "bin/CodeMetro.jar";
		ProcessBuilder pb = new ProcessBuilder("java", "-jar", new File(loc).getPath(), new File(target).getCanonicalPath());
		pb.redirectErrorStream(true);
		Process p = pb.start();
		
		String line = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		System.out.println("start");
		
		while ((line = in.readLine()) != null) {
            output.add(line);
		}
	}

	public void parseOutput() {
		for (String line : output){
			System.out.println("Checking " + line);
			if(line.matches(callStrRe)){
				System.out.println("Matched.");
				Matcher m = Pattern.compile(callStrRe).matcher(line);
				m.find();
				System.out.println(m.group(1) + " calls method " + m.group(2));
			}
		}
	}
}
