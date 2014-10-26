package codemetro.analyzer.gitinspector;

import java.io.*;

public class GitInspector {
	
	private static String loc = "lib/gitinspector-0.3.2/gitinspector/gitinspector.py";
	private static String target = ".";

	//Get output from gitinspector for a git repository
	public static void inspect(){
		try{
		System.out.println(new File(loc).getPath());
		System.out.println(new File(target).getCanonicalPath());
		
		// Build process to be run
		ProcessBuilder pb = new ProcessBuilder("python", new File(loc).getPath(), "-HTlr", new File(target).getCanonicalPath());
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
		}
//		while ((line = err.readLine()) != null) {  
//            System.out.println(line);
//		}
		System.out.println("finish");
		}catch(Exception e){System.out.println(e);}
		}
}
