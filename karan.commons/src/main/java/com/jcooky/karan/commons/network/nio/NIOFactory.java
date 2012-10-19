package com.jcooky.karan.commons.network.nio;

import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;

import com.jcooky.karan.commons.network.AbstractIOFactory;
import com.jcooky.karan.commons.network.Acceptor;
import com.jcooky.karan.commons.network.Connector;
import com.jcooky.karan.commons.network.Session;
import com.jcooky.karan.commons.network.Transfer;
import com.jcooky.karan.commons.network.Transport;
import com.jcooky.karan.commons.network.buffer.IoBuffer;

public class NIOFactory extends AbstractIOFactory {
	public static final String TYPE = "NIO";
	
	static {
		AbstractIOFactory.register(TYPE, NIOFactory.class);
	}
	
	private Connector connector = null;

	public Session createSession(Transport transport) {
		return new NIOSession(this, transport);
	}

	public <T> Transfer<T> createTransfer(Session session) {
		return (Transfer<T>)new NIOSpecTransfer(this, session);
	}

	public <T> Transfer<T> createTransfer(Transport transport) {
		return createTransfer(createSession(transport));
	}

	public Acceptor createAcceptor() {
		return new NIOAcceptor(this, Executors.defaultThreadFactory());
	}

	public Connector createConnector() {
		if (connector == null) {
			connector = new NIOConnector(this, Executors.defaultThreadFactory());
		}
		
		return connector;
	}

	public Transport createTransport(SocketChannel socketChannel,
			BlockingQueue<IoBuffer> readQ) {
		return new NIOTransport(this, socketChannel, readQ);
	}

	public IoBuffer createIoBuffer(int capacity) {
		return IoBuffer.allocate(capacity, true);
	}

	public IoBuffer createIoBuffer() {
		return createIoBuffer(1024);
	}
	
	public static AbstractIOFactory get() {
		return AbstractIOFactory.get(TYPE);
	}
}
