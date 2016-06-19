package uk.org.freedonia.jeonetstat.netstat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.junit.Test;

import uk.org.freedonia.mapnetstat.netstat.ProcessResult;
import uk.org.freedonia.mapnetstat.netstat.pidresolver.WindowsPidInfoReader;

public class TestWindowsPidInfoReader {

	@Test
	public void testWindowsPidResolver() throws IOException, InterruptedException {
		try ( BufferedReader reader = getReader() ) {
			Map<Integer, ProcessResult> results = runTest( reader );
			assertNotNull( results );
			assertEquals( 107, results.size() );
			assertEquals( "chrome.exe", results.get(9784).getExeName() );
			assertEquals( 9784, results.get(9784).getPid() );
			assertEquals( 107806720l, results.get(9784).getMemUsage() );			
		}
	}
	

	@Test
	public void testWindowsPidResolverWithNoResults() throws IOException, InterruptedException {
		try ( BufferedReader reader = getReaderWithString(  "\"Image Name\",\"PID\",\"Session Name\",\"Session#\",\"Mem Usage\"" ) ) {
			Map<Integer, ProcessResult> results = runTest( reader );
			assertNotNull( results );
			assertTrue( results.isEmpty() );
		}
	}
	
	@Test
	public void testWindowsPidResolverWithNoData() throws IOException, InterruptedException {
		try ( BufferedReader reader = getReaderWithString("" ) ) {
			Map<Integer, ProcessResult> results = runTest( reader );
			assertNotNull( results );
			assertTrue( results.isEmpty() );
		}
	}
	
	@Test
	public void testWindowsPidResolverWithGibberish() throws IOException, InterruptedException {
		try ( BufferedReader reader = getReaderWithString( "2345r82hn22khj5t2litu2kljth2,m2oi5235h23,5jwfjskjhsvhs" ) ) {
			Map<Integer, ProcessResult> results = runTest( reader );
			assertNotNull( results );
			assertTrue( results.isEmpty() );
		}
	}
	
	private Map<Integer, ProcessResult> runTest( BufferedReader reader ) throws IOException {
		WindowsPidInfoReader  pidReader = new WindowsPidInfoReader();
		Map<Integer, ProcessResult> results = pidReader.parseResults(reader);
		return results;
	}
	
	private BufferedReader getReader() {
		return new BufferedReader(new InputStreamReader(TestWindowsPidInfoReader.class.getResourceAsStream("/testdata/wintasklist.log")));
	}
	
	private BufferedReader getReaderWithString( String data ) {
		return new BufferedReader( new InputStreamReader( new ByteArrayInputStream( data.getBytes() ) ) );
	}
		

	
	
}
