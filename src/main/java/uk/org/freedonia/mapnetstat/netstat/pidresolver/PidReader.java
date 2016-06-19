package uk.org.freedonia.mapnetstat.netstat.pidresolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import uk.org.freedonia.mapnetstat.netstat.ProcessResult;

public interface PidReader {
	
	Map<Integer, ProcessResult> parseResults( BufferedReader reader ) throws IOException;


}
