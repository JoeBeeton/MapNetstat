package uk.org.freedonia.jeonetstat;

import java.net.InetAddress;
import java.net.UnknownHostException;

import uk.org.freedonia.mapnetstat.geo.GeoResult;
import uk.org.freedonia.mapnetstat.netstat.ConnectionResult;

public class CukeToGeoResultConverter {
	
	public static GeoResult getGeoResultFromCukeGeoResult( CukeGeoResult cukeGeo ) throws UnknownHostException {
		GeoResult result = new GeoResult();
		result.setCity( cukeGeo.getCity() );
		result.setCountryName( cukeGeo.getCountry() );
		result.setIp( cukeGeo.getIp() );
		result.setLatitude( cukeGeo.getLatitude() );
		result.setLongitude( cukeGeo.getLongitude() );
		ConnectionResult connResult = new ConnectionResult( InetAddress.getByName( cukeGeo.getIp() ),
				cukeGeo.getPort(), cukeGeo.getPid(), cukeGeo.getProcessName(), cukeGeo.getMemory() );
		result.setConnResult( connResult );
		return result;
	}

}
