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
import uk.org.freedonia.mapnetstat.netstat.pidresolver.LinuxPidInfoReader;



public class TestLinuxPidInfoReader {

	@Test
	public void testLinuxPidResolver() throws IOException, InterruptedException {
		try ( BufferedReader reader = getReader() ) {
			Map<Integer, ProcessResult> results = runTest( reader );
			assertNotNull( results );
			assertEquals( 165, results.size() );
			assertEquals( "/usr/lib/firefox/firefox", results.get(1797).getExeName() );
			assertEquals( 1797, results.get(1797).getPid() );
			assertEquals( 975008l, results.get(1797).getMemUsage() );			
		}
	}
	

	@Test
	public void testLinuxPidResolverWithNoResults() throws IOException, InterruptedException {
		try ( BufferedReader reader = getReaderWithString(  "  PID    VSZ USER" ) ) {
			Map<Integer, ProcessResult> results = runTest( reader );
			assertNotNull( results );
			assertTrue( results.isEmpty() );
		}
	}
	
	@Test
	public void testLinuxPidResolverWithNoData() throws IOException, InterruptedException {
		try ( BufferedReader reader = getReaderWithString("" ) ) {
			Map<Integer, ProcessResult> results = runTest( reader );
			assertNotNull( results );
			assertTrue( results.isEmpty() );
		}
	}
	
	@Test
	public void testLinuxPidResolverWithGibberish() throws IOException, InterruptedException {
		try ( BufferedReader reader = getReaderWithString( "2345r82hn22khj5t2litu2kljth2,m2oi5235h23,5jwfjskjhsvhs" ) ) {
			Map<Integer, ProcessResult> results = runTest( reader );
			assertNotNull( results );
			assertTrue( results.isEmpty() );
		}
	}
	
	private Map<Integer, ProcessResult> runTest( BufferedReader reader ) throws IOException {
		LinuxPidInfoReader  pidReader = new LinuxPidInfoReader();
		Map<Integer, ProcessResult> results = pidReader.parseResults(reader);
		return results;
	}
	
	private BufferedReader getReader() {
		return new BufferedReader(new InputStreamReader(TestWindowsPidInfoReader.class.getResourceAsStream("/testdata/linuxtasklist.log")));
	}
	
	private BufferedReader getReaderWithString( String data ) {
		return new BufferedReader( new InputStreamReader( new ByteArrayInputStream( data.getBytes() ) ) );
	}
	
		
		
	
	
	
	
}
