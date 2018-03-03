package uk.org.freedonia.mapnetstat.netstat.cmd;

import org.apache.commons.lang3.SystemUtils;

public class NetstatCommandResolverFactory {

	public static NetstatCommandResolver getNetstatCommandResolver() {
		if(SystemUtils.IS_OS_WINDOWS) {
            return new WindowsNetstatCommandResolver();
        } else {
		    return new LinuxNetstatCommandResolver();
        }
	}


	
	
}
