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
import uk.org.freedonia.mapnetstat.netstat.outputparser.WindowsNetstatOutputParser;
import uk.org.freedonia.mapnetstat.netstat.pidresolver.PidCommandExecutor;

public class TestWindowsNetstatOutputParser {
	
	@Test
	public void testWindowsNetstatOutputParser() throws IOException, InterruptedException {
		WindowsNetstatOutputParser parser = new WindowsNetstatOutputParser( getPidResolver() );
		try (BufferedReader br = new BufferedReader(new InputStreamReader(getTestData()))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	parser.netStatOutputConsumer().accept(line);
		    }
		}
		List<ConnectionResult> results = parser.getAddresses();
		assertNotNull( results );
		assertEquals( 85, results.size() );
		boolean matchFound = false;
		for ( ConnectionResult result : results ) {
			if ( result.getPid() == 4752 ) {
				assertNotNull( result.getProcessName() );
				assertEquals( "test.exe", result.getProcessName() );
				assertEquals( 123000l, result.getMemUsage() );
				matchFound = true;
			}
		}
		assertTrue( matchFound );	
	}
	
	private PidCommandExecutor getPidResolver() {
		return new PidCommandExecutor(null, null) {
			@Override
			public HashMap<Integer, ProcessResult> executePidCommand() throws IOException, InterruptedException {
				HashMap<Integer,ProcessResult> pidResults = new HashMap<>();
				ProcessResult procResult = new ProcessResult(4752, "test.exe", 123000 );
				pidResults.put(4752, procResult);
				return pidResults;
			}
		};
	}
	
	
	private InputStream getTestData() {
		return TestWindowsNetstatOutputParser.class.getResourceAsStream("/testdata/winstat.log");
	}

}
