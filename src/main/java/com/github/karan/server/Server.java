package com.github.karan.server;

import com.github.jcooky.mina.thrift.TMinaServer;
import com.github.karan.server.gateway.GatewayImpl;
import com.github.karan.server.gateway.gen.Gateway;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: jcooky
 * Date: 13. 1. 16.
 * Time: 오전 11:18
 * To change this template use File | Settings | File Templates.
 */
public class Server {
    private TMinaServer server;
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

    public void serve(int port) throws IOException {
        server = TMinaServer.getServer(port,
                new Gateway.Processor <Gateway.Iface>(new GatewayImpl()));

        server.serve();
    }

    public void stop() {
        if (server != null) {
            server.stop();
        }
    }
}
