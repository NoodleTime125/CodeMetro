package codemetro.fuser;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Test;

import codemetro.analyzer.callgraph.CallGraphRunner;
import codemetro.analyzer.gitinspector.GitInspector;
import codemetro.fuser.Fuser;

public class FuserTester {
	@Test
	public void testMock() throws IOException {
		// GIT INSPECTOR STUFF
		/*ArrayList<String> words = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader("./test/mockGitInspect.txt"));
		String line;
		while ((line = reader.readLine()) != null) {
		    words.add(line);
		}
		reader.close();
		git.output = words;
		*/
		GitInspector git = new GitInspector();
		git.inspect("./lib/core.jar");
		//System.out.println("git done");
		
		// CALL GRAPH STUFF
		CallGraphRunner instance = new CallGraphRunner();
		//instance.generateGraph("lib/core.jar");
		//System.out.println("call graph done");
		
		Fuser f = new Fuser(git.parseOutput(), instance.generateGraph("lib/core.jar"));
		
	}


}
