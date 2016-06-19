package uk.org.freedonia.mapnetstat.netstat.outputparser;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import uk.org.freedonia.mapnetstat.netstat.ConnectionResult;
import uk.org.freedonia.mapnetstat.netstat.ProcessResult;
import uk.org.freedonia.mapnetstat.netstat.pidresolver.PidCommandExecutor;

public class LinuxNetstatOutputParser implements NetstatOutputParser {
	
	private Map<Integer, ProcessResult> pidProcessMap;
	private NetStatConsumer netStatConsumer;
	private final List<ConnectionResult> addresses = Collections.synchronizedList(new ArrayList<ConnectionResult>());

	public LinuxNetstatOutputParser( PidCommandExecutor pidResolver ) throws IOException, InterruptedException {
		this.pidProcessMap = pidResolver.executePidCommand();	
		this.netStatConsumer = new NetStatConsumer();
	}

	@Override
	public Consumer<String> netStatOutputConsumer() {
		return netStatConsumer;
	}
	


	@Override
	public List<ConnectionResult> getAddresses() {
		return addresses;
	}
	
	class NetStatConsumer implements Consumer<String> {

		@Override
		public void accept(String line) {
			if ( line != null && line.trim().toLowerCase().startsWith("tcp") || line.trim().toLowerCase().startsWith("udp")) {
				List<String> ipSubList = Arrays.asList( line.trim().split(" ") ).stream().filter( seg -> ( !seg.trim().toLowerCase().equals("tcp") || !seg.trim().toLowerCase().equals("udp")  )&& seg.contains(":")   ).collect(Collectors.toList());
				if ( ipSubList.size() >= 2 ) {
					try {
						if ( !ipSubList.get(1).split(":")[0].contains("*") ) {
							
							InetAddress ip = InetAddress.getByName(ipSubList.get(1).split(":")[0]);
							try {
								int port = Integer.parseInt( ipSubList.get(1).split(":")[1].trim() );
								String[] parts = line.trim().split(" ");
								String procNamePidSegment = parts[parts.length-1];
								if ( procNamePidSegment.contains("/") ) {
									int pid = Integer.parseInt( procNamePidSegment.split("/")[0] );
									
									ProcessResult result = pidProcessMap.get(pid);
									
									if ( result != null ) {
										addresses.add(  new ConnectionResult( ip, port, pid, result.getExeName(), result.getMemUsage() ) );
									} else {
										addresses.add(  new ConnectionResult( ip, port, pid, "", 0 ) );
									}
								}
								
							} catch( NumberFormatException nfe ) {
								
							}
							
						}
					} catch (UnknownHostException e) {
						
					}
				}
			}
		}
			
	}

}
