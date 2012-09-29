package com.jcooky.karan.commons.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.jcooky.karan.commons.AbstractIOFactory;
import com.jcooky.karan.commons.Connector;
import com.jcooky.karan.commons.Transport;
import com.jcooky.karan.commons.buffer.IoBuffer;

public class NIOConnector implements Connector {
	private AbstractIOFactory ioFactory;
	private Map<String, BlockingQueue<IoBuffer>> queueMapper = new HashMap<String, BlockingQueue<IoBuffer>>();
	
	private NIOConnector(AbstractIOFactory factory) {
		this.ioFactory = factory;
	}
	
	public Transport connect(InetAddress host, int port) {
		SocketChannel socketChannel;
		NIOReadSelector nioReadSelector;
		BlockingQueue<IoBuffer> q = new LinkedBlockingQueue<IoBuffer>();
		
		try {
			Selector readSelector = Selector.open();
			socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
			socketChannel.configureBlocking(false);
			socketChannel.register(readSelector, SelectionKey.OP_READ);
			
			nioReadSelector = new NIOReadSelector(this.ioFactory, readSelector, queueMapper);
			
		} catch(UnknownHostException e) {
			throw new NIOException(e);
		} catch(IOException e) {
			throw new NIOException(e);
		}
		
		NIOTransport nioTransport = (NIOTransport)ioFactory.createTransport(socketChannel, q);
		queueMapper.put(nioTransport.getId(), q);
		
		return nioTransport;
	}

}
