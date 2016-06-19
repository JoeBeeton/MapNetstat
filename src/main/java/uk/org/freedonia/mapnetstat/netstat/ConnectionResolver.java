package uk.org.freedonia.mapnetstat.netstat;

import java.io.IOException;
import java.util.List;

public interface ConnectionResolver {

	List<ConnectionResult> resolveIPList( ConnectionResolverOptions options ) throws IOException, InterruptedException;
	
	
}
