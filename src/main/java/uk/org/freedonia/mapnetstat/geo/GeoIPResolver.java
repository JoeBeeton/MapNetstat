package uk.org.freedonia.mapnetstat.geo;

import java.io.IOException;
import java.net.InetAddress;

public interface GeoIPResolver {
	
	GeoResult getCountryFromIP( InetAddress ip ) throws IOException;

}
