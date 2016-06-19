package uk.org.freedonia.mapnetstat.netstat.pidresolver;

import java.io.IOException;
import java.util.Map;

import uk.org.freedonia.mapnetstat.netstat.ProcessResult;

public interface PidInfoResolver {
	
	
	Map<Integer,ProcessResult> resolveRunningProcesses() throws IOException, InterruptedException;

}
