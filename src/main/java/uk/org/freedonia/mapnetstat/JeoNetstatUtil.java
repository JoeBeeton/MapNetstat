package uk.org.freedonia.mapnetstat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import uk.org.freedonia.mapnetstat.geo.FreeGeoIPResolver;
import uk.org.freedonia.mapnetstat.geo.GeoIPResolver;
import uk.org.freedonia.mapnetstat.geo.GeoResult;
import uk.org.freedonia.mapnetstat.netstat.ConnectionResolver;
import uk.org.freedonia.mapnetstat.netstat.ConnectionResolverOptions;
import uk.org.freedonia.mapnetstat.netstat.ConnectionResult;
import uk.org.freedonia.mapnetstat.netstat.NetstatIPResolver;


/**
 * JeoNetstatUtil is a Utility class which acts as an entry point into the JoeNetstat Library.
 * It will make a list of all active network connections on this machine, attempt to map the remote endpoints of those
 * connections using a GeoIP database along with current processes on this machine and returns the results.
 * @author Joe Beeton
 *
 */
public class JeoNetstatUtil {
	


	/**
	 * Returns a list of all current open connections. Along with other information useful for determining the reason for the 
	 * connection. Such as the process that is making the connection and the location of the remote destination of that connection.
	 * @param options the options for filtering the open connections
	 * @return a list of GeoResults representing the current network connections of this machine
	 * @throws IOException if a problem occurs while resolving the network connections
	 * @throws InterruptedException if a problem occurs while resolving the network connections
	 */
	public static List<GeoResult> getCurrentConnections( ConnectionResolverOptions options ) throws IOException, InterruptedException {
		return getCurrentConnections( options, getGeoIPResolver(), getResolver() );
	}


	/**
	 * Returns a list of all current open connections. Along with other information useful for determining the reason for the 
	 * connection. Such as the process that is making the connection and the location of the remote destination of that connection.
	 * @param options the options for filtering the open connections
	 * @param geoIPResolver the GeoIPResolver implementation used to resolve information about an IP Address.
	 * @param connectionResolver the ConnectionResolver used to list the current open network connections.
	 * @return a list of GeoResults representing the current network connections of this machine
	 * @throws IOException if a problem occurs while resolving the network connections
	 * @throws InterruptedException if a problem occurs while resolving the network connections
	 */
	public static List<GeoResult> getCurrentConnections( ConnectionResolverOptions options, 
			GeoIPResolver geoIPResolver, 
			ConnectionResolver connectionResolver ) throws IOException, InterruptedException {
		List<GeoResult> geoResults = Collections.synchronizedList( new ArrayList<GeoResult>() );
		List<ConnectionResult> addresses = connectionResolver.resolveIPList( options );
		InetAddressGeoResolveConsumer consumer = new InetAddressGeoResolveConsumer( geoResults, geoIPResolver );
		addresses.stream().parallel().forEach( consumer );
		return geoResults;
	}
	
	private static class InetAddressGeoResolveConsumer implements Consumer<ConnectionResult> {
		private List<GeoResult> results;
		private GeoIPResolver geoIPResolver;

		public InetAddressGeoResolveConsumer( List<GeoResult> results, GeoIPResolver geoIPResolver ) {
			this.results = results;
			this.geoIPResolver = geoIPResolver;
		}

		@Override
		public void accept( ConnectionResult t ) {
			try {
				GeoResult geo = geoIPResolver.getCountryFromIP( t.getIp() );
				if ( geo != null ) { 
					geo.setConnResult(t);
					results.add( geo );
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static GeoIPResolver getGeoIPResolver() {
		return new FreeGeoIPResolver();
	}
	
	private static ConnectionResolver getResolver() {
		return new NetstatIPResolver();
	}
	
	
	
}
