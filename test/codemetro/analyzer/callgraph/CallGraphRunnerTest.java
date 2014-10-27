package codemetro.analyzer.callgraph;

import org.junit.Test;
import java.io.IOException;

public class CallGraphRunnerTest {

	@Test
	public void test() throws IOException{
		CallGraphRunner instance = new CallGraphRunner();
		instance.generateGraph();
		instance.parseOutput();
	}
}
