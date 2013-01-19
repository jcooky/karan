package com.github.karan;

import com.github.karan.server.Server;
import com.github.karan.server.gateway.gen.Gateway;
import com.github.karan.test.TestServiceImpl;
import com.github.karan.test.gen.TestInfo;
import com.github.karan.test.gen.TestService;
import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jcooky
 * Date: 13. 1. 16.
 * Time: 오전 11:56
 * To change this template use File | Settings | File Templates.
 */
public class ServerTest {
    private Gateway.Iface client;
    private TSocket socket;
    private Server server;
    private int port = 8081;

    @Before
    public void setUp() throws Exception {
        server = new Server();
        server.serve(port);

        socket = new TSocket("localhost", port);
        socket.open();
        TProtocol protocol = new TBinaryProtocol(socket);
        client = new Gateway.Client(protocol);
    }

    @After
    public void tearDown() throws Exception {
        socket.close();
        server.stop();
        Thread.sleep(1000);
    }

    @Test
    public void testServe() {
        Assert.assertTrue(true);
    }

    public void testExists(String _ifaceName) throws Exception {
        String ifaceName = StringUtils.defaultString(_ifaceName, "test");

        boolean result = client.exists(ifaceName);
        Assert.assertFalse(result);
    }

    @Test
    public void testExists() throws Exception {
        testExists(null);
    }

    @Test
    public void testPut() throws Exception {
        List<String> names = new ArrayList<String>();
        names.add(TestInfo.class.getName());
        names.add(TestService.class.getName());
        names.add(TestServiceImpl.class.getName());

        List<ByteBuffer> binaries = new ArrayList<ByteBuffer>();
        binaries.add(ByteBuffer.wrap(FileUtils.readFileToByteArray(new File("target/test-classes/com/github/karan/test/gen/TestInfo.class"))));
        binaries.add(ByteBuffer.wrap(FileUtils.readFileToByteArray(new File("target/test-classes/com/github/karan/test/gen/TestService.class"))));
        binaries.add(ByteBuffer.wrap(FileUtils.readFileToByteArray(new File("target/test-classes/com/github/karan/test/TestServiceImpl.class"))));

        client.put(TestService.class.getName(), TestServiceImpl.class.getName(), names, binaries);

        testExists(TestService.class.getName());
    }
}
