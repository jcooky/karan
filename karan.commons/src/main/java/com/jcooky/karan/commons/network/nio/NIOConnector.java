package com.jcooky.karan.commons.network.nio;

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
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcooky.karan.commons.network.AbstractIOFactory;
import com.jcooky.karan.commons.network.Connector;
import com.jcooky.karan.commons.network.Transport;
import com.jcooky.karan.commons.network.buffer.IoBuffer;
import com.jcooky.karan.commons.network.listeners.ChannelCloseListener;
import com.jcooky.karan.commons.network.listeners.TransportCloseListener;
import com.jcooky.karan.commons.util.MultiSetRunner;

public class NIOConnector implements Connector {
	private static final Logger logger = LoggerFactory.getLogger(NIOConnector.class);
	
	private AbstractIOFactory ioFactory;
	private ThreadFactory threadFactory;
	
	private Map<String, BlockingQueue<IoBuffer>> queueMapper = new HashMap<String, BlockingQueue<IoBuffer>>();
	private MultiSetRunner runner = new MultiSetRunner();
	
	public NIOConnector(AbstractIOFactory factory, ThreadFactory threadFactory) {
		this.ioFactory = factory;
		this.threadFactory = threadFactory;
	}
	
	public Transport connect(InetAddress host, int port) {
		SocketChannel socketChannel;
		BlockingQueue<IoBuffer> q = new LinkedBlockingQueue<IoBuffer>();
		
		try {
			final NIOReadSelector readSelector = new NIOReadSelector(this.ioFactory, Selector.open(), queueMapper);
			socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
			final Transport transport = (NIOTransport)ioFactory.createTransport(socketChannel, q);
			logger.debug("connecting seucceded");
			socketChannel.configureBlocking(false);
			socketChannel.register(readSelector.selector(), SelectionKey.OP_READ, transport.getId());
			
			readSelector.addForceCloseListener(new ChannelCloseListener() {

				public void onClosed(String channelId) {
					transport.close();
					runner.stop();
				}
				
			});
			
			transport.addCloseListener(new TransportCloseListener() {

				public void onClosed(Transport transport) {
					runner.stop();
				}
				
			});
			
			queueMapper.put(transport.getId(), q);
			
			runner.add(readSelector);
			threadFactory.newThread(runner).start();
			
			return transport;
		} catch(UnknownHostException e) {
			throw new NIOException(e);
		} catch(IOException e) {
			throw new NIOException(e);
		}
	}

}
