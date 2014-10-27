package codemetro.analyzer.callgraph;

import org.junit.Test;
import java.io.IOException;

import junit.framework.TestCase;

public class CallGraphRunnerTest extends TestCase{

	@Test
	public void test() throws IOException{
		CallGraphRunner instance = new CallGraphRunner();
		instance.generateGraph("lib/core.jar");
		instance.parseOutput();
	}
}
