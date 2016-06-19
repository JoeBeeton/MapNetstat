package uk.org.freedonia.mapnetstat.geo;

import java.io.IOException;
import java.net.InetAddress;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Country;

public class FreeGeoIPResolver implements GeoIPResolver {

	
	public GeoResult getCountryFromIP( InetAddress ip ) throws IOException {
		try {
			DatabaseReader reader = getDbReader();
			CityResponse response = reader.city( ip );
			Country country = response.getCountry();
			
			return new GeoResult( 
					ip.getHostAddress(),
					country.getIsoCode(),
					country.getName(),
					response.getCity().getName(),
					response.getLocation().getLatitude(),
					response.getLocation().getLongitude() );
		} catch ( GeoIp2Exception e ) {
			GeoResult basicResult = new GeoResult();
			basicResult.setIp(ip.getHostAddress());
			return basicResult;
		}
	}

	private DatabaseReader getDbReader() throws IOException {
		return new DatabaseReader.Builder( FreeGeoIPResolver.class.getResourceAsStream("/GeoLite2-City.mmdb") ).build();
	}
	
	
	

}
