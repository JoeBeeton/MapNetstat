package uk.org.freedonia.jeonetstat.gui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

import uk.org.freedonia.mapnetstat.geo.GeoResult;
import uk.org.freedonia.mapnetstat.gui.GeoResultProperty;
import uk.org.freedonia.mapnetstat.netstat.ConnectionResult;

public class TestGeoResultProperty {
	
	
	@Test
	public void testPidResolution() throws UnknownHostException {
		GeoResultProperty prop = new GeoResultProperty( getDummyResult() );
		assertEquals( new Integer( 4321 ), prop.getPid() );
	}
	
	@Test
	public void testNullPidResolution() throws UnknownHostException {
		GeoResultProperty prop = new GeoResultProperty( getNullDummyResult() );
		assertEquals( new Integer( 0 ), prop.getPid() );

	}
	
	@Test
	public void testIPResolution() throws UnknownHostException {
		GeoResultProperty prop = new GeoResultProperty( getDummyResult() );
		assertEquals( "8.8.8.8", prop.getIpAddress() );
	}
	
	@Test
	public void testNullIPResolution() throws UnknownHostException {
		GeoResultProperty prop = new GeoResultProperty( getNullDummyResult() );
		assertNull( prop.getIpAddress() );
	}

	@Test
	public void testLatitudeResolution() throws UnknownHostException {
		GeoResultProperty prop = new GeoResultProperty( getDummyResult() );
		assertEquals( new Double( 1.2d ), prop.getLatitude() );
	}
	
	@Test
	public void testNullLatitudeResolution() throws UnknownHostException {
		GeoResultProperty prop = new GeoResultProperty( getNullDummyResult() );
		assertEquals( new Double( 0.0d ), prop.getLatitude() );
	}
	
	@Test
	public void testLongitude() throws UnknownHostException {
		GeoResultProperty prop = new GeoResultProperty( getDummyResult() );
		assertEquals( new Double( 3.4d ), prop.getLongitude() );
	}
	
	@Test
	public void testNullLongitudeResolution() throws UnknownHostException {
		GeoResultProperty prop = new GeoResultProperty( getNullDummyResult() );
		assertEquals( new Double( 0.0d ), prop.getLongitude() );
	}
	
	@Test
	public void testCity() throws UnknownHostException {
		GeoResultProperty prop = new GeoResultProperty( getDummyResult() );
		assertEquals( "Gotham", prop.getCity() );
	}
	
	@Test
	public void testCountry() throws UnknownHostException {
		GeoResultProperty prop = new GeoResultProperty( getDummyResult() );
		assertEquals( "United Kingdom", prop.getCountry() );
	}
	
	@Test
	public void testProcessName() throws UnknownHostException {
		GeoResultProperty prop = new GeoResultProperty( getDummyResult() );
		assertEquals( "someApp.exe", prop.getProcessName() );
	}
	
	@Test
	public void testPort() throws UnknownHostException {
		GeoResultProperty prop = new GeoResultProperty( getDummyResult() );
		assertEquals( new Integer( 1234 ), prop.getPort() );
	}
	
	@Test
	public void testMemoryUsage() throws UnknownHostException {
		GeoResultProperty prop = new GeoResultProperty( getDummyResult() );
		assertEquals( new Long( 32000l ), prop.getMemoryUsage() );
	}
	
	
	private GeoResult getDummyResult() throws UnknownHostException {
		GeoResult dummy = new GeoResult();
		dummy.setIp("8.8.8.8");
		dummy.setLatitude(1.2d);
		dummy.setLongitude(3.4d);
		dummy.setCity("Gotham");
		dummy.setCountryCode("UK");
		dummy.setCountryName("United Kingdom");
		ConnectionResult connResult = new ConnectionResult( InetAddress.getByName("8.8.8.8"), 1234, 4321, "someApp.exe", 32000 );
		dummy.setConnResult(connResult);
		return dummy;
	}
	
	private GeoResult getNullDummyResult() throws UnknownHostException {
		GeoResult dummy = new GeoResult();
		dummy.setIp(null);
		dummy.setLatitude(null);
		dummy.setLongitude(null);
		dummy.setCity(null);
		dummy.setCountryCode(null);
		dummy.setCountryName(null);
		dummy.setConnResult(null);
		return dummy;
	}

}
