package codemetro.analyzer.gitinspector.test;

import static org.junit.Assert.*;

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


}
