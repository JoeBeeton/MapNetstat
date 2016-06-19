package uk.org.freedonia.mapnetstat.geo;

import uk.org.freedonia.mapnetstat.netstat.ConnectionResult;

public class GeoResult {

	private String ip;
	private String countryCode;
	private String countryName;
	private String city;
	private Double latitude;
	private Double longitude;
	private ConnectionResult connResult;
	
	
	public GeoResult(String ip, String countryCode, String countryName, String city, Double latitude, Double longitude ) {
		this.ip = ip;
		this.countryCode = countryCode;
		this.countryName = countryName;
		this.city = city;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public GeoResult(){}
	
	


	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeoResult other = (GeoResult) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		return true;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public ConnectionResult getConnResult() {
		return connResult;
	}

	public void setConnResult(ConnectionResult connResult) {
		this.connResult = connResult;
	}
}
