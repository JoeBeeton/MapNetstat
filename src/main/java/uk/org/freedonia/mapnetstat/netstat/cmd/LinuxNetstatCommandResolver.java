package uk.org.freedonia.mapnetstat.netstat.cmd;

import uk.org.freedonia.mapnetstat.netstat.ConnectionResolverOptions;

public class LinuxNetstatCommandResolver implements NetstatCommandResolver {


    @Override
    public String resolveCommand(ConnectionResolverOptions opts) {
        StringBuilder cmd = new StringBuilder();
        cmd.append("netstat -apn");
        switch( opts.getProtocolOptions() ) {
            case ALL:
                cmd.append("tu");
                break;
            case TCP:
                cmd.append("t");
                break;
            case UDP:
                cmd.append("u");
                break;
            default:
                break;
        }
        return cmd.toString();
    }
}
