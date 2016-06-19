package uk.org.freedonia.jeonetstat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mockito.Mockito;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import uk.org.freedonia.mapnetstat.JeoNetstatUtil;
import uk.org.freedonia.mapnetstat.geo.FreeGeoIPResolver;
import uk.org.freedonia.mapnetstat.geo.GeoResult;
import uk.org.freedonia.mapnetstat.netstat.ConnectionResolverOptions;
import uk.org.freedonia.mapnetstat.netstat.ConnectionResult;
import uk.org.freedonia.mapnetstat.netstat.NetstatIPResolver;
import uk.org.freedonia.mapnetstat.netstat.ProtocolOptions;
import uk.org.freedonia.mapnetstat.netstat.outputparser.LinuxNetstatOutputParser;
import uk.org.freedonia.mapnetstat.netstat.outputparser.NetstatOutputParser;
import uk.org.freedonia.mapnetstat.netstat.pidresolver.PidInfoResolver;

public class JeoNetstatUtilGlue {

	
	private boolean localConnection;
	private ProtocolOptions opts;
	private Collection<GeoResult> results;
	private InputStream connectionStream;
	private InputStream pidStream;
	
	@Given("^The Local Connection flag is set to \"([^\"]*)\"$")
	public void the_local_connection_flag_is_set_to(String flag) throws Throwable {
		localConnection = Boolean.getBoolean(flag);
	}
	
	@Then("^the following results are returned$")
	public void the_following_results_are_returned( List<CukeGeoResult> table ) throws Throwable {
		results = table.parallelStream().map( new GeoConverter() ).collect(Collectors.<GeoResult>toList() );
	}
	
	private class GeoConverter implements Function<CukeGeoResult, GeoResult> {
			@Override
			public GeoResult apply(CukeGeoResult cukeGeo) {
				GeoResult result = new GeoResult();
				result.setCity( cukeGeo.getCity() );
				result.setCountryName( cukeGeo.getCountry() );
				result.setIp( cukeGeo.getIp() );
				result.setLatitude( cukeGeo.getLatitude() );
				result.setLongitude( cukeGeo.getLongitude() );
				try {
					ConnectionResult connResult = new ConnectionResult( InetAddress.getByName( cukeGeo.getIp() ),
							cukeGeo.getPort(), cukeGeo.getPid(), cukeGeo.getProcessName(), cukeGeo.getMemory() );
					result.setConnResult( connResult );
				} catch (UnknownHostException e) {
					fail( e.getMessage() );
				}
				return result;
			}
			
	}
	

    @And("^The Protocol Options is set to \"([^\"]*)\"$")
    public void the_protocol_options_is_set_to_something( String protocolOpts) throws Throwable {
        opts = ProtocolOptions.valueOf(protocolOpts);
    }
	
	@And("^JeoNetStatUtil.getCurrentConnections is called$")
	public void jeonetstatutilgetcurrentconnections_is_called() throws Throwable {
		ConnectionResolverOptions options = new ConnectionResolverOptions( opts, localConnection );
		results = JeoNetstatUtil.getCurrentConnections(options, new FreeGeoIPResolver(), getNetstatIPResolver() );
		assertNotNull( results );
		assertFalse( results.isEmpty() );
	}
	
	private PidInfoResolver getPidInfoResolver() {
		pidStream = this.getClass().getResourceAsStream( "/testdata/linuxtasklist.log" );
		return null;
	}
	
	
	private NetstatIPResolver getNetstatIPResolver() {
		return new NetstatIPResolver(){
			
			@Override
			protected Process executeCommand( ConnectionResolverOptions options ) throws IOException {
				Process proc = Mockito.mock(Process.class);
				Mockito.when(proc.getInputStream()).thenReturn(getStreamToLinuxLog());
				return proc;
			}
			
			@Override
			protected Consumer<String> getConsumer() {
				NetstatOutputParser linuxParser = null;
				try {
					linuxParser = new LinuxNetstatOutputParser(null);
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
				return linuxParser.netStatOutputConsumer();
			}
		};
		
	}
	
	private InputStream getStreamToLinuxLog() {
		connectionStream = this.getClass().getResourceAsStream( "/testdata/linuxstat.log" );
		return connectionStream;
	}


	
	
	

}
