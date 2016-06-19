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

public class WindowsNetstatOutputParser implements NetstatOutputParser {
	
	private final List<ConnectionResult> addresses = Collections.synchronizedList(new ArrayList<ConnectionResult>());
	private Map<Integer,ProcessResult> pidProcessMap;
	private Consumer<String> netStatConsumer;
	
	public WindowsNetstatOutputParser(PidCommandExecutor pidResolver ) throws IOException, InterruptedException {
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
			if ( line != null && line.trim().startsWith("TCP") || line.trim().startsWith("UDP")) {
				List<String> ipSubList = Arrays.asList( line.trim().split(" ") ).stream().filter( seg -> ( !seg.trim().equals("TCP") || !seg.trim().equals("UDP")  )&& seg.contains(":")   ).collect(Collectors.toList());
				if ( ipSubList.size() >= 2 ) {
					try {
						if ( !ipSubList.get(1).split(":")[0].contains("*") ) {
							
							InetAddress ip = InetAddress.getByName(ipSubList.get(1).split(":")[0]);
							int port = Integer.parseInt( ipSubList.get(1).split(":")[1].trim() );
							String[] parts = line.trim().split(" ");
							int pid = Integer.parseInt( parts[parts.length-1] );
							ProcessResult result = pidProcessMap.get(pid);
							
							if ( result != null ) {
								addresses.add(  new ConnectionResult( ip, port, pid, result.getExeName(), result.getMemUsage() ) );
							} else {
								addresses.add(  new ConnectionResult( ip, port, pid, "", 0 ) );
							}
							
						}
					} catch (UnknownHostException e) {
						
					}
				}
			}
		}
			
	}

}
