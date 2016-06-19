package uk.org.freedonia.mapnetstat.netstat.pidresolver;

import org.apache.commons.lang3.SystemUtils;

public class PidInfoResolverFactory {

	
	public static PidCommandExecutor getPidInfoResovler() {
		if ( SystemUtils.IS_OS_WINDOWS ) {
			return new PidCommandExecutor( "tasklist -fo CSV", new WindowsPidInfoReader() );
		} else {
			return new PidCommandExecutor( "ps -A -o pid,vsz,user,cmd", new LinuxPidInfoReader() );
		}
	}
	
	
	
	
}
