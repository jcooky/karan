package com.github.karan.server;

import com.github.jcooky.mina.thrift.TIoAcceptorServerTransport;
import com.github.jcooky.mina.thrift.TIoSessionTransport;
import com.github.jcooky.mina.thrift.TMinaServer;
import com.github.karan.server.gateway.GatewayImpl;
import com.github.karan.server.gateway.gen.Gateway;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TTransportFactory;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: jcooky
 * Date: 13. 1. 16.
 * Time: 오전 11:18
 * To change this template use File | Settings | File Templates.
 */
public class Server {
    private int port = 7900;
    private TServer server;
    private Gateway.Iface gateway ;

    public Server() {
        gateway = new GatewayImpl();
    }

    public void setGateway(Gateway.Iface gateway) {
        this.gateway = gateway;
    }

    public Gateway.Iface getGateway() {
        return gateway;
    }

    public void setServer(TServer server) {
        this.server = server;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void serve() throws IOException {
        if (this.server == null) {
            TIoAcceptorServerTransport socket = new TIoAcceptorServerTransport(port);
            TProcessor processor = new Gateway.Processor <Gateway.Iface>(new GatewayImpl());
            server = new TMinaServer(new TMinaServer.Args(socket)
                    .processor(processor)
                    .protocolFactory(new TCompactProtocol.Factory())
                    .inputTransportFactory(new TIoSessionTransport.InputTransportFactory())
                    .outputTransportFactory(new TTransportFactory()));
        }

        server.serve();
    }

    public void stop() {
        if (server != null) {
            server.stop();
        }
    }
}
