package uk.org.freedonia.mapnetstat.netstat.outputparser;

import java.util.List;
import java.util.function.Consumer;

import uk.org.freedonia.mapnetstat.netstat.ConnectionResult;

public interface NetstatOutputParser {

	
	Consumer<String> netStatOutputConsumer();
	
	List<ConnectionResult> getAddresses();
	
}
