package codemetro.analyzer.gitinspector;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitInspector {
	
	private static String loc = "lib/gitinspector-0.3.2/gitinspector/gitinspector.py";
//	private static String target = ".";
	
	public static String regExName = "^(.*) is mostly responsible for:$";
	public static String regExLines = "\\s*(\\d*) (.*)\\.java$";
	public ArrayList<String> output = new ArrayList<String>();

	//Get output from gitinspector for a git repository
	public void inspect(String target){
		try{
		System.out.println(new File(loc).getPath());
		System.out.println(new File(target).getCanonicalPath());
		
		// Build process to be run
		ProcessBuilder pb = new ProcessBuilder(
				"python", 
				new File(loc).getPath(),
				"-HTlr",
				new File(target).getCanonicalPath());
		pb.environment().put("PYTHONIOENCODING", "UTF-8");
		pb.redirectErrorStream(true);
		Process p = pb.start();
		
		String line = null;
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
//		BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		System.out.println("start");
		
		// print gitinspector output
		while ((line = in.readLine()) != null) {  
            System.out.println(line);
            output.add(line);
		}
//		while ((line = err.readLine()) != null) {  
//            System.out.println(line);
//		}
		System.out.println("finish");
		}catch(Exception e){System.out.println(e);}
		}
	
	public ArrayList<GitInspectorEntry> parseOutput() {
		ArrayList<GitInspectorEntry> list= new ArrayList<GitInspectorEntry>();
		GitInspectorEntry gie = null;
		Integer x;
		for (String line : output){
			System.out.println("Checking " + line);
			if(line.matches(regExName)){
				System.out.println("Matched.");
				Matcher m = Pattern.compile(regExName).matcher(line);
				m.find();
				gie = new GitInspectorEntry(m.group(1));
				list.add(gie);
				System.out.println("gie set to "+m.group(1));
			}
			else if(line.matches(regExLines)) {
				Matcher m = Pattern.compile(regExLines).matcher(line);
				m.find();
				System.out.println(m.group(1) + " lines written for " + m.group(2));
				x = Integer.valueOf(m.group(1));
				gie.fileEntry(x, m.group(2));
			}
		}
		System.out.println(list);
		return list;
	}
}
