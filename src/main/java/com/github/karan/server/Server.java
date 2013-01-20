package com.github.karan.server;

import com.github.jcooky.mina.thrift.TMinaServer;
import com.github.karan.server.gateway.GatewayImpl;
import com.github.karan.server.gateway.gen.Gateway;
import org.apache.thrift.server.TServer;

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
            server = TMinaServer.getServer(port,
                    new Gateway.Processor <Gateway.Iface>(new GatewayImpl()));
        }

        server.serve();
    }

    public void stop() {
        if (server != null) {
            server.stop();
        }
    }
}
