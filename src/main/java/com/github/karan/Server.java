package com.github.karan;

import com.github.jcooky.mina.thrift.TIoAcceptorServerTransport;
import com.github.jcooky.mina.thrift.TMinaServer;

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

    public void serve(int port) throws IOException {
        server = new TMinaServer(new TMinaServer.Args(
                new TIoAcceptorServerTransport(port)
        ));

        server.serve();
    }

    public void stop() {
        server.stop();
    }
}
