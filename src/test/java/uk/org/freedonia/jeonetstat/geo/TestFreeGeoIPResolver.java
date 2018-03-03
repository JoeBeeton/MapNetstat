package uk.org.freedonia.jeonetstat.geo;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import uk.org.freedonia.mapnetstat.geo.FreeGeoIPResolver;
import uk.org.freedonia.mapnetstat.geo.GeoResult;

public class TestFreeGeoIPResolver {

	@Test
	public void test() throws  IOException {
		InetAddress ip = InetAddress.getByName("8.8.8.8");
		GeoResult geoResult = new FreeGeoIPResolver().getCountryFromIP(ip);
		assertNotNull( geoResult );
	}

	@Test
	public void testWithValidIP() throws  IOException {
		InetAddress ip = InetAddress.getByName("84.45.230.235");
		GeoResult geoResult = new FreeGeoIPResolver().getCountryFromIP(ip);
		assertNotNull( geoResult );

	}
	
	
}
