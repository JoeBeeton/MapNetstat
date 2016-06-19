package uk.org.freedonia.mapnetstat.gui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import uk.org.freedonia.mapnetstat.geo.GeoResult;

public class GeoResultProperty {
	
	private final StringProperty country = new SimpleStringProperty("");
	private final StringProperty city = new SimpleStringProperty("");
	private final StringProperty ipAddress = new SimpleStringProperty("");
	private final StringProperty processName = new SimpleStringProperty("");
	private final LongProperty memoryUsage = new SimpleLongProperty(0);
	private final IntegerProperty port = new SimpleIntegerProperty(0);
	private final IntegerProperty pid = new SimpleIntegerProperty(0);
	private final DoubleProperty latitude = new SimpleDoubleProperty(0d);
	private final DoubleProperty longitude = new SimpleDoubleProperty(0d);

	
	
	
	private GeoResult geoResult;

	public GeoResultProperty( GeoResult geoResult ) {
		this.geoResult = geoResult;
		refreshValues( geoResult );
	}
	
	private void refreshValues( GeoResult geoResult ) {
		if ( geoResult.getConnResult() != null ) {
			setPid( geoResult.getConnResult().getPid() );	
			setProcessName( geoResult.getConnResult().getProcessName() );
			setMemoryUsage( geoResult.getConnResult().getMemUsage() );
			setPort( geoResult.getConnResult().getPort() );
		}
		setCountry( geoResult.getCountryName() );
		setCity( geoResult.getCity() );
		setIPAddress( geoResult.getIp() );
		setLatitude( geoResult.getLatitude() );
		setLongitude( geoResult.getLongitude() );
	}
	
	
	
	public GeoResult getGeoResult() {
		return geoResult;
	}
	
	
	
	public String getCountry() {
		return country.get();
	}
	
	public void setCountry( String countryValue ) {
		country.set(countryValue);
	}

	public String getCity() {
		return city.get();
	}
	
	public void setCity( String cityValue ) {
		city.set(cityValue);
	}

	public String getIpAddress() {
		return ipAddress.get();
	}
	
	public void setIPAddress( String ipAddressValue ) {
		ipAddress.set(ipAddressValue);
	}

	public String getProcessName() {
		return processName.get();
	}
	
	public void setProcessName( String processNameValue ) {
		processName.set(processNameValue);
	}
	

	public Long getMemoryUsage() {
		return memoryUsage.get();
	}
	
	public void setMemoryUsage( long memoryUsageValue ) {
		memoryUsage.set(memoryUsageValue);
	}

	public Integer getPort() {
		return port.get();
	}
	
	public void setPort( Integer portValue ) {
		if ( portValue != null ) {
			port.set(portValue);
		}
	}

	public Integer getPid() {
		return pid.get();
	}
	
	public void setPid( Integer pidValue ) {
		if ( pidValue != null ) {
			pid.set(pidValue);
		}
	}

	public Double getLatitude() {
		return latitude.get();
	}
	
	public void setLatitude( Double latitudeValue ) {
		if ( latitudeValue != null ) {
			latitude.set(latitudeValue);

		}
	}

	public Double getLongitude() {
		return longitude.get();
	}
	
	public void setLongitude( Double longitudeValue ) {
		if ( longitudeValue != null ) {
			longitude.set(longitudeValue);
		}
	}


	
	
	
	
}
