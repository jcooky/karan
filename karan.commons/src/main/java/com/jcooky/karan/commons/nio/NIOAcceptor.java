package com.jcooky.karan.commons.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcooky.karan.commons.AbstractIOFactory;
import com.jcooky.karan.commons.Acceptor;
import com.jcooky.karan.commons.Transport;
import com.jcooky.karan.commons.buffer.IoBuffer;

public class NIOAcceptor implements Acceptor, Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(NIOAcceptor.class);
	
	private ServerSocketChannel serverSocketChannel;
	private NIOReadSelector readSelector;
	private AbstractIOFactory ioFactory;
	
	private Map<String, BlockingQueue<IoBuffer>> queueMapper = new HashMap<String, BlockingQueue<IoBuffer>>();
	private Map<String, Transport> transportMapper = new HashMap<String, Transport>();
	private Queue<Transport> acceptQ = new LinkedList<Transport>();
	
	public NIOAcceptor(AbstractIOFactory ioFactory, int port) {
		this.ioFactory = ioFactory;
		try {
			// 넌 블럭킹 서버소켓채널을 연다 (디폴트는 블럭킹)
			this.serverSocketChannel = ServerSocketChannel.open();
			this.serverSocketChannel.configureBlocking(false);

			// 로컬 주소와 포트에 바인딩
			this.serverSocketChannel.socket().bind(
					new InetSocketAddress(port));

			// 클라이언트 채널을 멀티플렉싱하는 셀렉터 얻기
			this.readSelector = new NIOReadSelector(ioFactory, Selector.open(), queueMapper);

		} catch (IOException e) {
			logger.error("error initializing server", e);
			throw new RuntimeException(e);
		}
	}

	public Transport accept() {
		return acceptQ.isEmpty() ? null : acceptQ.remove();
	}
	
	private void addNewClient(SocketChannel channel) {
		BlockingQueue<IoBuffer> q = new LinkedBlockingQueue<IoBuffer>();
		Transport transport = null;
		try {
			transport = this.ioFactory.createTransport(channel, q);
			
			channel.configureBlocking(false);
			SelectionKey readKey = channel.register(this.readSelector.selector(),
					SelectionKey.OP_READ, new StringBuffer());
		} catch (ClosedChannelException cce) {
			logger.error(cce.getMessage(), cce);
			throw new RuntimeException(cce);
		} catch (IOException ioe) {
			logger.error(ioe.getMessage(), ioe);
			throw new RuntimeException(ioe);
		}
		
		this.acceptQ.add(transport);
		this.queueMapper.put(transport.getId(), q);
	}
	
	public void run() {
		SocketChannel clientChannel;
		try {
			while ((clientChannel = this.serverSocketChannel.accept()) != null) {
				addNewClient(clientChannel);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
}
