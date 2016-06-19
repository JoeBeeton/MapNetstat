package uk.org.freedonia.mapnetstat.netstat.cmd;

import uk.org.freedonia.mapnetstat.netstat.ConnectionResolverOptions;

public interface NetstatCommandResolver {

	
	String resolveCommand( ConnectionResolverOptions opts );
	
	
}
