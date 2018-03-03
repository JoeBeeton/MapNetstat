package uk.org.freedonia.mapnetstat.netstat.cmd;

import org.junit.Test;
import uk.org.freedonia.mapnetstat.netstat.ConnectionResolverOptions;
import uk.org.freedonia.mapnetstat.netstat.ProtocolOptions;

import static org.junit.Assert.*;

public class WindowsNetstatCommandResolverTest {

    @Test
    public void testWithAllProtocols() {
        assertEquals("netstat -ano", new WindowsNetstatCommandResolver().resolveCommand(new ConnectionResolverOptions(ProtocolOptions.ALL, false)));
    }

    @Test
    public void testWithUDP() {
        assertEquals("netstat -ano -p UDP", new WindowsNetstatCommandResolver().resolveCommand(new ConnectionResolverOptions(ProtocolOptions.UDP, false)));
    }

    @Test
    public void testWithTCP() {
        assertEquals("netstat -ano -p TCP", new WindowsNetstatCommandResolver().resolveCommand(new ConnectionResolverOptions(ProtocolOptions.TCP, false)));
    }


}