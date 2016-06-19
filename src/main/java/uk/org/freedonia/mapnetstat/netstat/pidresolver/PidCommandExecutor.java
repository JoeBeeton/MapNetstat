package uk.org.freedonia.mapnetstat.netstat.pidresolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import uk.org.freedonia.mapnetstat.netstat.ProcessResult;

public class PidCommandExecutor {

	private String execString;
	private PidReader pidReader;

	public PidCommandExecutor( String execString, PidReader pidReader ) {
		this.execString = execString;
		this.pidReader = pidReader;
	}
	
	public Map<Integer, ProcessResult> executePidCommand() throws IOException, InterruptedException {
		Process proc = Runtime.getRuntime().exec( execString );
		return parseResults( proc, pidReader );
	}

	

	private Map<Integer, ProcessResult> parseResults( Process proc, PidReader pidReader ) throws IOException, InterruptedException {
		Map<Integer, ProcessResult> results = new HashMap<>();
		try ( BufferedReader reader = 
                new BufferedReader(new InputStreamReader(proc.getInputStream()));
                ) {
			results = pidReader.parseResults(reader);
		}
		proc.waitFor();
		return results;
	}
 
	
	
}
