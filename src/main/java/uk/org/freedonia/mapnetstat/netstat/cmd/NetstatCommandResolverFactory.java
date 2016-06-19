package uk.org.freedonia.mapnetstat.netstat.cmd;

public class NetstatCommandResolverFactory {

	public static NetstatCommandResolver getNetstatCommandResolver() {
		return new WindowsNetstatCommandResolver();
	}
	
	
}
