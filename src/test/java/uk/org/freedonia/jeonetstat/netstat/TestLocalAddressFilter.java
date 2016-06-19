package uk.org.freedonia.jeonetstat.netstat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import uk.org.freedonia.mapnetstat.netstat.ConnectionResult;
import uk.org.freedonia.mapnetstat.netstat.LocalAddressFilter;

public class TestLocalAddressFilter {

	
	
	@Test
	public void testWithSingleLocalHostConnection() throws UnknownHostException {
		LocalAddressFilter filter = new LocalAddressFilter();
		List<ConnectionResult> connResults = Arrays.asList( getResultWithIP( "127.0.0.1") );
		assertTrue( filter.filterLocalConnections(connResults).isEmpty() );
	}
	
	@Test
	public void testWithSinglePrivateIP() throws UnknownHostException {
		LocalAddressFilter filter = new LocalAddressFilter();
		List<ConnectionResult> connResults = Arrays.asList( getResultWithIP( "192.168.1.1") );
		assertTrue( filter.filterLocalConnections(connResults).isEmpty() );
	}
	
	
	@Test
	public void testWithSinglePublicIP() throws UnknownHostException {
		LocalAddressFilter filter = new LocalAddressFilter();
		List<ConnectionResult> connResults = Arrays.asList( getResultWithIP("8.8.8.8" ) );
		assertFalse( filter.filterLocalConnections(connResults).isEmpty() );
	}
	
	@Test
	public void testWithSinglePublicIPAndSinglePrivateIP() throws UnknownHostException {
		LocalAddressFilter filter = new LocalAddressFilter();
		List<ConnectionResult> connResults = Arrays.asList( getResultWithIP("8.8.8.8"), getResultWithIP( "192.168.1.1" ) );
		List<ConnectionResult> results = filter.filterLocalConnections( connResults );
		assertTrue( results.size()  == 1 );
		assertTrue( results.get(0).getIp().getHostAddress().equals("8.8.8.8") );
	}
	
	@Test
	public void testWithIPsFromAllPrivateNetworks() throws UnknownHostException {
		LocalAddressFilter filter = new LocalAddressFilter();
		List<ConnectionResult> connResults = Arrays.asList( 
				getResultWithIP("172.16.2.1"),
				getResultWithIP( "192.168.1.1" ),
				getResultWithIP( "10.2.1.4" )
		);
		assertTrue( filter.filterLocalConnections( connResults ).isEmpty() );
	}
	
	@Test
	public void testBoundaryConditionsFor10Network() throws UnknownHostException {
		LocalAddressFilter filter = new LocalAddressFilter();
		List<ConnectionResult> connResults = Arrays.asList( 
				getResultWithIP("9.255.255.255"),
				getResultWithIP( "10.0.0.0" ),
				getResultWithIP( "10.0.0.1" ),
				getResultWithIP( "10.255.255.255" ),
				getResultWithIP( "11.0.0.0" )
		);
		List<ConnectionResult> results = filter.filterLocalConnections( connResults );
		assertEquals( 2, results.size() );
		assertTrue( results.contains(getResultWithIP("9.255.255.255")) );
		assertTrue( results.contains( getResultWithIP( "11.0.0.0" ) ) );
	}
	
	@Test
	public void testBoundaryConditionsFor172Network() throws UnknownHostException {
		LocalAddressFilter filter = new LocalAddressFilter();
		List<ConnectionResult> connResults = Arrays.asList( 
				getResultWithIP("172.15.255.255"),
				getResultWithIP( "172.16.0.0" ),
				getResultWithIP( "172.16.0.1" ),
				getResultWithIP( "172.16.255.255" ),
				getResultWithIP( "172.17.0.0" )
		);
		List<ConnectionResult> results = filter.filterLocalConnections( connResults );
		assertEquals( 2, results.size() );
		assertTrue( results.contains(getResultWithIP("172.15.255.255")) );
		assertTrue( results.contains( getResultWithIP( "172.17.0.0" ) ) );
	}
	
	@Test
	public void testBoundaryConditionsFor192Network() throws UnknownHostException {
		LocalAddressFilter filter = new LocalAddressFilter();
		List<ConnectionResult> connResults = Arrays.asList( 
				getResultWithIP("192.167.255.255"),
				getResultWithIP( "192.168.0.0" ),
				getResultWithIP( "192.168.0.1" ),
				getResultWithIP( "192.168.255.255" ),
				getResultWithIP( "192.169.0.0" )
		);
		List<ConnectionResult> results = filter.filterLocalConnections( connResults );
		assertEquals( 2, results.size() );
		assertTrue( results.contains(getResultWithIP("192.167.255.255")) );
		assertTrue( results.contains( getResultWithIP( "192.169.0.0" ) ) );
	}
	
	
	
	private ConnectionResult getResultWithIP( String ip ) throws UnknownHostException {
		return new ConnectionResult(InetAddress.getByName(ip), 0, 0, null, 0);
	}
}
