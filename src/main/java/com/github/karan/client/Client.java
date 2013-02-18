package com.github.karan.client;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.github.karan.server.gateway.gen.Gateway;

public class Client {
	private static final int SOCKET_TIMEOUT = 10 * 1000;
	private Gateway.Iface clientIface = null;

	private TTransport transport;

	public Gateway.Iface getGayeway() throws TTransportException {
		return clientIface;
	}

	public void connect(String host, int port) throws TTransportException {
		transport = new TSocket(host, port, SOCKET_TIMEOUT);
		transport = new TFramedTransport(transport);
		final TProtocol protocol = new TBinaryProtocol(transport);
		final Gateway.Client client = new Gateway.Client(protocol);

		// The transport must be opened before you can begin using
		transport.open();

		clientIface = client;
	}

	public void close() {
		if (transport != null && transport.isOpen()) {
			transport.close();
		}
	}
}
