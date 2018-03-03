package uk.org.freedonia.mapnetstat.netstat.outputparser;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import uk.org.freedonia.mapnetstat.netstat.ConnectionResult;
import uk.org.freedonia.mapnetstat.netstat.ProcessResult;
import uk.org.freedonia.mapnetstat.netstat.pidresolver.PidCommandExecutor;

/**
 * LinuxNetstatOutputParser is used to parse the output of the netstat command on Linux. The output of the netstat
 * should look something like this.
 * The output is passed to this class one line at a time.
 * Proto Recv-Q Send-Q Local Address           Foreign Address         State       PID/Program name
 * tcp        0      0 127.0.1.1:53            0.0.0.0:*               LISTEN      -
 * tcp        0      0 127.0.0.1:631           0.0.0.0:*               LISTEN      -
 * tcp        0      0 10.0.2.15:51158         173.194.137.102:443     TIME_WAIT   -
 * tcp        0      0 10.0.2.15:51154         173.194.137.102:443     ESTABLISHED 8922/firefox
 * tcp        0      0 10.0.2.15:53902         216.58.206.106:443      ESTABLISHED 8922/firefox
 *
 * The output of the netstat command columns are not ordered and can change between implementations.
 * So for example PID/Program column may be the first column not the last. The way round this is this
 * class detects the line containing the column headers. The data starts at the same index point as the start of
 * the column name. So for example State starts at index point 43 for example. If we record that and look at a data
 * line,  the start of the State type will also start at index point 43 as netstat formats the output in that manner.
 *
 */
public class LinuxNetstatOutputParser implements NetstatOutputParser {

    private Map<LinuxNetstatHeaders,Integer> headerMapping = new HashMap<>();
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
            if(isHeaderLine(line)) {
                populateHeaderMapping(line);
            } else {
                if (!headerMapping.isEmpty()) {
                    if (isProtocolValid(line)&&isStateValid(line)) {
                        try {
                            ConnectionResult result = getIPAndPort(getValueForHeader(line, LinuxNetstatHeaders.FOREIGN_ADDRESS));
                            if (result != null) {
                                String pidName = getValueForHeader(line, LinuxNetstatHeaders.PID_PROC);
                                result.setPid(getPid(pidName));
                                ProcessResult procResult = getProcessResult(pidName);
                                if (procResult != null) {
                                    result.setMemUsage(procResult.getMemUsage());
                                    result.setProcessName(procResult.getExeName());
                                    result.setPid(procResult.getPid());
                                }
                                addresses.add(result);
                            }
                        } catch (UnknownHostException | NumberFormatException e) {
                        }
                    }
                }
            }
        }

        /**
         * Takes the ipPort segment of the command output which should look like "173.194.137.102:443" and
         * returns a new ConnectionResult populated with the ip and port.
         * @param ipPort the ip and port string.
         * @return a new ConnectionResult populated with the ip and port. or null if the string is invalid
         * @throws UnknownHostException if the ip is not valid
         * @throws NumberFormatException if the port is not valid
         */
        private ConnectionResult getIPAndPort(String ipPort) throws UnknownHostException, NumberFormatException {
            ConnectionResult result = null;
            if(StringUtils.countMatches(ipPort, ".")==3 && ipPort.contains(":")) {
                InetAddress ip =   InetAddress.getByName(ipPort.trim().split(":")[0]);
                int port = Integer.parseInt( ipPort.trim().split(":")[1]);
                result = new ConnectionResult(ip, port, 0,"",0);
            }
            return result;
        }

        /**
         * populates the headerMapping map, with the index points of the starts of those headers.
         * @param line the line containing the column headers.
         */
        private void populateHeaderMapping(String line) {
            for(LinuxNetstatHeaders header : LinuxNetstatHeaders.values()) {
                headerMapping.put(header, line.toLowerCase().indexOf(header.getHeaderKey()));
            }
        }

        /**
         * returns the value from the line for the specified header.
         * @param line the line of output from the netstat command
         * @param header the header of the value to be returned
         * @return the value for the header.
         */
        private String getValueForHeader(String line, LinuxNetstatHeaders header) {
            String linePart = line.substring(headerMapping.get(header), line.length() );
            return linePart.split(" ")[0];
        }

        /**
         * returns true if the specified line is a header line.
         * @param line the line to be checked
         * @return true if the line is a header line, else returns false.
         */
        private boolean isHeaderLine(String line) {
            if(headerMapping.isEmpty()) {
                return Arrays.stream(LinuxNetstatHeaders.values()).allMatch(e -> line.toLowerCase().contains(e.getHeaderKey().toLowerCase()));
            } else{
                return false;
            }
        }

        /**
         * returns the ProcessResult ( if it exists ) for the specified pid. Which needs to be in the format
         *  1234/firefox
         * @param pid the pid string
         * @return the ProcessResult or null if it cannot be found for the specified pid
         */
        private ProcessResult getProcessResult(String pid) {
            if (pid.contains("/")) {
                int pidInt = Integer.parseInt(pid.split("/")[0]);
                return pidProcessMap.get(pidInt);
            } else {
                return null;
            }
        }

        /**
         * returns the pid as an int
         * @param pid the pid string
         * @return the pid.
         * @throws NumberFormatException if the pid string is not valid
         */
        private int getPid(String pid) throws NumberFormatException {
            if (pid.contains("/")) {
                return Integer.parseInt(pid.split("/")[0]);
            }
            else {
                return 0;
            }
        }

        /**
         * returns true if the protocol is valid. This is either udp or tcp
         * @param line the data line
         * @return true if tcp or udp
         */
        private boolean isProtocolValid(String line) {
            String proto = getValueForHeader(line, LinuxNetstatHeaders.PROTO);
            return proto.equals("tcp")||proto.equals("udp");
        }

        /**
         * returns true if the connection state is established.
         * @param line the data line
         * @return true if connection is established
         */
        private boolean isStateValid(String line) {
            String state = getValueForHeader(line, LinuxNetstatHeaders.STATE);
            return state.equalsIgnoreCase("established");
        }


    }

}
