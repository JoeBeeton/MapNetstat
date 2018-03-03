package uk.org.freedonia.mapnetstat.netstat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.List;
import java.util.function.Consumer;

import uk.org.freedonia.mapnetstat.netstat.cmd.NetstatCommandResolver;
import uk.org.freedonia.mapnetstat.netstat.cmd.NetstatCommandResolverFactory;
import uk.org.freedonia.mapnetstat.netstat.outputparser.NetStatOutputParserFactory;
import uk.org.freedonia.mapnetstat.netstat.outputparser.NetstatOutputParser;
import uk.org.freedonia.mapnetstat.netstat.pidresolver.PidInfoResolverFactory;

public class NetstatIPResolver implements ConnectionResolver {
	

	private NetstatOutputParser netstatParser;
	
	public List<ConnectionResult> resolveIPList( ConnectionResolverOptions options ) throws IOException, InterruptedException {
		netstatParser = getNetStatOutputParser();
		Process proc = executeCommand(options);
		parseResults(proc);
		return filterInternalIPAddresses(options);
	}
	
	private NetstatOutputParser getNetStatOutputParser() throws IOException, InterruptedException {
		return NetStatOutputParserFactory.getNetStatOutputParser( PidInfoResolverFactory.getPidInfoResovler());
	}
	
	protected Process executeCommand( ConnectionResolverOptions options ) throws IOException {
		return Runtime.getRuntime().exec( getNetStatCommand( options ) );
	}
	
	private String getNetStatCommand( ConnectionResolverOptions options ) {
		NetstatCommandResolver resolver = NetstatCommandResolverFactory.getNetstatCommandResolver();
		return resolver.resolveCommand(options);
	}
	
	private List<ConnectionResult> filterInternalIPAddresses( ConnectionResolverOptions options ) throws UnknownHostException {
		if ( options.isDisplayLocalNetworkResults() ) {
			return netstatParser.getAddresses();
		} else {
			return new LocalAddressFilter().filterLocalConnections(netstatParser.getAddresses());
		}
	}
	
	
	private void parseResults( Process proc ) throws IOException, InterruptedException {
		try ( BufferedReader reader = 
                        new BufferedReader(new InputStreamReader(proc.getInputStream())) ) {
		      reader.lines().forEach( getConsumer() );
		}
		proc.waitFor();
	}

	protected Consumer<String> getConsumer() {
		return netstatParser.netStatOutputConsumer();
	}

	
	
	
}
