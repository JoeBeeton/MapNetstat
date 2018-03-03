package uk.org.freedonia.jeonetstat.netstat.outputparser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import uk.org.freedonia.mapnetstat.netstat.ConnectionResult;
import uk.org.freedonia.mapnetstat.netstat.ProcessResult;
import uk.org.freedonia.mapnetstat.netstat.outputparser.LinuxNetstatOutputParser;
import uk.org.freedonia.mapnetstat.netstat.pidresolver.PidCommandExecutor;

public class TestLinuxNetstatOutputParser {

	@Test
	public void testLinuxNetstatOutputParser() throws IOException, InterruptedException {
		LinuxNetstatOutputParser parser = new LinuxNetstatOutputParser( getPidResolver() );
		try (BufferedReader br = new BufferedReader(new InputStreamReader(getTestData()))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	parser.netStatOutputConsumer().accept(line);
		    }
		}
		List<ConnectionResult> results = parser.getAddresses();
		assertNotNull( results );
		assertEquals( 22, results.size() );
		boolean matchFound = false;
		for ( ConnectionResult result : results ) {
			if ( result.getPid() == 1926 ) {
				assertNotNull( result.getProcessName() );
				assertEquals( "firefox", result.getProcessName() );
				assertEquals( 123000, result.getMemUsage() );
				matchFound = true;
			}
		}
		assertTrue( matchFound );	
	}
	
	private PidCommandExecutor getPidResolver() {
		return new PidCommandExecutor(null, null) {
			@Override
			public HashMap<Integer, ProcessResult> executePidCommand() {
				HashMap<Integer,ProcessResult> pidResults = new HashMap<>();
				ProcessResult procResult = new ProcessResult(1926, "firefox", 123000 );
				pidResults.put(1926, procResult);
				return pidResults;
			}
		};
	}
	
	
	private InputStream getTestData() {
		return TestLinuxNetstatOutputParser.class.getResourceAsStream("/testdata/linuxstat.log");
	}
	
}
