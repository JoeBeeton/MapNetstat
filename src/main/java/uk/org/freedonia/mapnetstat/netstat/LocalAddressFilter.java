package uk.org.freedonia.mapnetstat.netstat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LocalAddressFilter {
	
	private static HashMap<Long,Long> privateRanges = null;

	public List<ConnectionResult> filterLocalConnections( List<ConnectionResult>  connectionResults ) throws UnknownHostException {
		return connectionResults.stream().parallel().filter(isExternalPredicate()).distinct().collect(Collectors.toList());
	}
	
	private static Predicate<ConnectionResult> isExternalPredicate() {
		return p -> !p.getIp().getHostAddress().equals("0.0.0.0") &&  !p.getIp().getHostAddress().equals("127.0.0.1") && !isInPrivateIPRange(p.getIp());
	}
	
	private static boolean isInPrivateIPRange( InetAddress ip ) {
		long longIP = ipToLong( ip );
		boolean isPrivate = false;
		HashMap<Long, Long> addressRanges;
		try {
			addressRanges = getPrivateAddressRanges();
			for ( Long ipStart : addressRanges.keySet() ) {
				Long ipEnd = addressRanges.get(ipStart);
				if ( longIP >= ipStart && longIP <=ipEnd ) {
					isPrivate = true;
					break;
				}
			}
		} catch (UnknownHostException e) {
		
		}
		
		return isPrivate;
		
	}
	
	private static long ipToLong(InetAddress ip) {
        byte[] octets = ip.getAddress();
        long result = 0;
        for (byte octet : octets) {
            result <<= 8;
            result |= octet & 0xff;
        }
        return result;
    }

	private static synchronized HashMap<Long,Long> getPrivateAddressRanges() throws UnknownHostException {
		if ( privateRanges == null ) {
			privateRanges = new HashMap<Long,Long>();
			privateRanges.put(ipToLong( InetAddress.getByName("10.0.0.0") ), ipToLong( InetAddress.getByName("10.255.255.255") ));
			privateRanges.put(ipToLong( InetAddress.getByName("172.16.0.0") ), ipToLong( InetAddress.getByName("172.16.255.255") ));
			privateRanges.put(ipToLong( InetAddress.getByName("192.168.0.0") ), ipToLong( InetAddress.getByName("192.168.255.255") ));
		}
		return privateRanges;
	}
	
}
