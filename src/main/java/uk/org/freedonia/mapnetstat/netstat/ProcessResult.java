package uk.org.freedonia.mapnetstat.netstat;

public class ProcessResult {

	private int pid;
	private String exeName;
	private long memUsage;	
	
	public ProcessResult(int pid, String exeName, long memUsage ) {
		this.pid = pid;
		this.exeName = exeName;
		this.memUsage = memUsage;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getExeName() {
		return exeName;
	}
	public void setExeName(String exeName) {
		this.exeName = exeName;
	}
	public long getMemUsage() {
		return memUsage;
	}
	public void setMemUsage(long memUsage) {
		this.memUsage = memUsage;
	}
	
	
}
