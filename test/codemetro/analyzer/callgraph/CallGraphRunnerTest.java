package codemetro.analyzer.callgraph;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class CallGraphRunnerTest extends TestCase{

	private CallGraphRunner instance = new CallGraphRunner();	// CallGraphRunner class
	
	@Test
	public void test() throws IOException{
		instance.generateGraph("lib/core.jar");
	}
	

	/**
	 * Tests the parsing function for parseOutput.
	 * @throws IOException
	 */
	/*@Test
	public void testParse() throws IOException{
		String fileName = "test/codemetro/analyzer/callgraph/mockCallGraphOutput.txt";
		List<String> output = new ArrayList<String>();
		BufferedReader in = new BufferedReader(new FileReader(fileName));
		String line;
		while ((line = in.readLine()) != null){
			output.add(line);
		}
		in.close();
		System.out.println("myInstance" + instance);
		Map<String, CallGraphNode> results = instance.parseOutput(output);
		//System.out.println(results);
		assertEquals(14, results.size());
	}*/
}
