package uk.org.freedonia.mapnetstat.netstat.cmd;

import org.junit.Test;
import uk.org.freedonia.mapnetstat.netstat.ConnectionResolverOptions;
import uk.org.freedonia.mapnetstat.netstat.ProtocolOptions;

import static org.junit.Assert.*;

public class LinuxNetstatCommandResolverTest {

    @Test
    public void testWithAllProtocols() {
        assertEquals("netstat -apntu", new LinuxNetstatCommandResolver().resolveCommand(new ConnectionResolverOptions(ProtocolOptions.ALL, false)));
    }

    @Test
    public void testWithUDP() {
        assertEquals("netstat -apnu", new LinuxNetstatCommandResolver().resolveCommand(new ConnectionResolverOptions(ProtocolOptions.UDP, false)));
    }

    @Test
    public void testWithTCP() {
        assertEquals("netstat -apnt", new LinuxNetstatCommandResolver().resolveCommand(new ConnectionResolverOptions(ProtocolOptions.TCP, false)));
    }



}