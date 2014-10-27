package codemetro.analyzer.gitinspector.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Test;

import codemetro.analyzer.gitinspector.GitInspector;

public class GitInspectorTest {
	
	GitInspector git = new GitInspector();
	
//	@Test
//	public void testInspect() {
//		git.inspect();
//	}
	
	@Test
	public void testParse() {
		git.inspect();
		System.out.println(git.parseOutput());
	}
	
	@Test
	public void testMock() throws IOException {
		ArrayList<String> words = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader("./test/mockGitInspect.txt"));
		String line;
		while ((line = reader.readLine()) != null) {
		    words.add(line);
		}
		reader.close();
		git.output = words;
		System.out.println(git.parseOutput());
	}


}
