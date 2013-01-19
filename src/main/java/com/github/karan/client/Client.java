package com.github.karan.client;


import com.github.karan.server.gateway.gen.Gateway;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;

public class Client {
    private static final int SOCKET_TIMEOUT = 10 * 1000;
    private static Gateway.Iface INSTANCE = null;

    public static Gateway.Iface getGayeway(String host, int port) throws TTransportException {
        if (INSTANCE == null) {
            final TSocket socket = new TSocket(host, port, SOCKET_TIMEOUT);
            final TProtocol protocol = new TBinaryProtocol(socket);
            final Gateway.Client client = new Gateway.Client(protocol);

            //The transport must be opened before you can begin using
            socket.open();

            INSTANCE = client;
        }

        return INSTANCE;
    }
}
