package uk.org.freedonia.mapnetstat.netstat.outputparser;

public enum LinuxNetstatHeaders {



    LOCAL_ADDRESS("local"),
    FOREIGN_ADDRESS("foreign"),
    PID_PROC("pid"),
    PROTO("proto"),
    STATE("state");

    private String headerKey;

    LinuxNetstatHeaders(String headerKey) {
        this.headerKey = headerKey;
    }

    public String getHeaderKey() {
        return headerKey;
    }





}
