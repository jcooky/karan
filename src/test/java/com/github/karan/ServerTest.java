package com.github.karan;

import com.github.karan.server.Server;
import com.github.karan.server.gateway.GatewayImpl;
import com.github.karan.server.gateway.gen.Gateway;
import com.github.karan.test.TestServiceImpl;
import com.github.karan.test.gen.TestInfo;
import com.github.karan.test.gen.TestService;
import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(ServerTest.class);

    private Gateway.Iface client;
    private TTransport socket;
    private Server server;
    private int port = 8081;

    @Before
    public void setUp() throws Exception {
        server = new Server();
//        server.setPort(8081);
        final TServerSocket serverSocket = new TServerSocket(port);
        new Thread() {
            public void run() {
                try {
                    serverSocket.listen();
                } catch(Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
        TServer tserver = new TSimpleServer(new TSimpleServer.Args(serverSocket)
                .transportFactory(new TTransportFactory())
                .protocolFactory(new TCompactProtocol.Factory())
                .processor(new Gateway.Processor(new GatewayImpl()))
        );
        server.setServer(tserver);
        server.serve();

        socket = new TSocket("localhost", port);
        socket.open();
        TProtocol protocol = new TCompactProtocol(socket);
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
        logger.info("calling testExists[_ifaceName={}]", _ifaceName);
        String ifaceName = StringUtils.defaultString(_ifaceName, "test");

        logger.info("exists remote call");
        boolean result = client.exists(ifaceName);
        logger.info("end exists remote call");
        Assert.assertFalse(result);
    }

    @Test
    public void testExists() throws Exception {
        testExists(null);
    }

    @Test
    public void testPut() throws Exception {
        logger.info("start testPut");
        List<String> names = new ArrayList<String>();
        names.add(TestInfo.class.getName());
        names.add(TestService.class.getName());
        names.add(TestServiceImpl.class.getName());

        List<ByteBuffer> binaries = new ArrayList<ByteBuffer>();
        binaries.add(ByteBuffer.wrap(FileUtils.readFileToByteArray(new File("target/test-classes/com/github/karan/test/gen/TestInfo.class"))));
        binaries.add(ByteBuffer.wrap(FileUtils.readFileToByteArray(new File("target/test-classes/com/github/karan/test/gen/TestService.class"))));
        binaries.add(ByteBuffer.wrap(FileUtils.readFileToByteArray(new File("target/test-classes/com/github/karan/test/TestServiceImpl.class"))));

        client.put(TestService.class.getName(), TestServiceImpl.class.getName(), names, binaries);
        logger.info("calling testExists");
        testExists(TestService.class.getName());
    }
}
