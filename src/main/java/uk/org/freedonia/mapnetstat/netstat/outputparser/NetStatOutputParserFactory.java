package uk.org.freedonia.mapnetstat.netstat.outputparser;

import java.io.IOException;

import org.apache.commons.lang3.SystemUtils;

import uk.org.freedonia.mapnetstat.netstat.pidresolver.PidCommandExecutor;

public class NetStatOutputParserFactory {

	
	public static NetstatOutputParser getNetStatOutputParser( PidCommandExecutor pidResolver ) throws IOException, InterruptedException {
		if ( SystemUtils.IS_OS_WINDOWS ) {
			return new WindowsNetstatOutputParser( pidResolver );
		} else {
			return new LinuxNetstatOutputParser( pidResolver );
		}
	}
	
	
}
