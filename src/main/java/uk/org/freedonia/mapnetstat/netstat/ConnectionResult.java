package uk.org.freedonia.mapnetstat.netstat;

import java.net.InetAddress;

public class ConnectionResult {


	private InetAddress ip;
	private int port;
	private int pid;
	private String processName;
	private long memUsage;

	
	public ConnectionResult(InetAddress ip, int port, int pid, String processName, long memUsage) {
		this.setIp(ip);
		this.setPort(port);
		this.setPid(pid);
		this.setProcessName(processName);
		this.setMemUsage(memUsage);
	}


	public InetAddress getIp() {
		return ip;
	}


	public void setIp(InetAddress ip) {
		this.ip = ip;
	}


	public int getPid() {
		return pid;
	}


	public void setPid(int pid) {
		this.pid = pid;
	}


	public long getMemUsage() {
		return memUsage;
	}


	public void setMemUsage(long memUsage) {
		this.memUsage = memUsage;
	}


	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}


	public String getProcessName() {
		return processName;
	}


	public void setProcessName(String processName) {
		this.processName = processName;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + (int) (memUsage ^ (memUsage >>> 32));
		result = prime * result + pid;
		result = prime * result + port;
		result = prime * result + ((processName == null) ? 0 : processName.hashCode());
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
		ConnectionResult other = (ConnectionResult) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (memUsage != other.memUsage)
			return false;
		if (pid != other.pid)
			return false;
		if (port != other.port)
			return false;
		if (processName == null) {
			if (other.processName != null)
				return false;
		} else if (!processName.equals(other.processName))
			return false;
		return true;
	}
	

	


}
