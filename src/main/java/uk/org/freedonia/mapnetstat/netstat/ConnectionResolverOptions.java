package uk.org.freedonia.mapnetstat.netstat;

public class ConnectionResolverOptions {
	
	
	private ProtocolOptions protocolOptions;
	private boolean displayLocalNetworkResults;
	
	
	public ConnectionResolverOptions( ProtocolOptions protocolOptions, boolean displayLocalNetworkResults) {
		this.protocolOptions = protocolOptions;
		this.displayLocalNetworkResults = displayLocalNetworkResults;
	}
	
	public ConnectionResolverOptions() {
		this( ProtocolOptions.TCP, false );
	}
	
	
	public ProtocolOptions getProtocolOptions() {
		return protocolOptions;
	}
	public void setProtocolOptions( ProtocolOptions protocolOptions) {
		this.protocolOptions = protocolOptions;
	}
	public boolean isDisplayLocalNetworkResults() {
		return displayLocalNetworkResults;
	}
	public void setDisplayLocalNetworkResults(boolean displayLocalNetworkResults) {
		this.displayLocalNetworkResults = displayLocalNetworkResults;
	}

}
