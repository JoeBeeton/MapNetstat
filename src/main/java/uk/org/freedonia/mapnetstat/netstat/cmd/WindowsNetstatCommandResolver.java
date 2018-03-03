package uk.org.freedonia.mapnetstat.netstat.cmd;

import uk.org.freedonia.mapnetstat.netstat.ConnectionResolverOptions;

public class WindowsNetstatCommandResolver implements NetstatCommandResolver {

	@Override
	public String resolveCommand( ConnectionResolverOptions opts ) {
		StringBuilder cmd = new StringBuilder();
		cmd.append("netstat -ano");
		switch( opts.getProtocolOptions() ) {
		case ALL:
			break;
		case TCP:
			cmd.append(" -p TCP");
			break;
		case UDP:
			cmd.append(" -p UDP");
			break;
		default:
			break;
		
		}
		return cmd.toString();
	}

}
