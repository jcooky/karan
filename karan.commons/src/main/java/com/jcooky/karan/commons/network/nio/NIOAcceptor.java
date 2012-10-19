package com.jcooky.karan.commons.network.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcooky.karan.commons.network.AbstractIOFactory;
import com.jcooky.karan.commons.network.Acceptor;
import com.jcooky.karan.commons.network.Transport;
import com.jcooky.karan.commons.network.buffer.IoBuffer;
import com.jcooky.karan.commons.network.listeners.ChannelCloseListener;
import com.jcooky.karan.commons.network.listeners.TransportCloseListener;
import com.jcooky.karan.commons.util.MultiSetRunner;

public class NIOAcceptor implements Acceptor, Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(NIOAcceptor.class);

	private ServerSocketChannel serverSocketChannel;
	private NIOReadSelector readSelector = null;
	private AbstractIOFactory ioFactory;
	private ThreadFactory threadFactory;

	private Map<String, BlockingQueue<IoBuffer>> queueMapper = Collections.synchronizedMap(new HashMap<String, BlockingQueue<IoBuffer>>());
	private Map<String, Transport> transportMapper = Collections.synchronizedMap(new HashMap<String, Transport>());
	private BlockingQueue<Transport> acceptQ = new LinkedBlockingQueue<Transport>();
	private MultiSetRunner runner = new MultiSetRunner(); 

	public NIOAcceptor(AbstractIOFactory ioFactory, ThreadFactory threadFactory) {
		this.ioFactory = ioFactory;
		this.threadFactory = threadFactory;
	}
	
	public void setIdleTime(int idleTime) {
		runner.setIdleTime(idleTime);
	}
	
	public void listen(int port) {
		try {
			// 넌 블럭킹 서버소켓채널을 연다 (디폴트는 블럭킹)
			this.serverSocketChannel = ServerSocketChannel.open();
			this.serverSocketChannel.configureBlocking(false);

			// 로컬 주소와 포트에 바인딩
			this.serverSocketChannel.socket().bind(new InetSocketAddress(port));
			logger.debug("bind completed");

			// 클라이언트 채널을 멀티플렉싱하는 셀렉터 얻기
			this.readSelector = new NIOReadSelector(ioFactory, Selector.open(),
					queueMapper);
			this.readSelector.addForceCloseListener(new ChannelCloseListener() {

				public void onClosed(String channelId) {
					queueMapper.remove(channelId);
					transportMapper.get(channelId).close();
					transportMapper.remove(channelId);
				}
				
			});
			runner.add(this);
			runner.add(this.readSelector);
			threadFactory.newThread(runner).start();
		} catch (IOException e) {
			logger.error("error initializing server", e);
			throw new RuntimeException(e);
		}
	}

	public Transport accept() {
		try {
			return acceptQ.isEmpty() ? null : acceptQ.take();
		} catch (InterruptedException e) {
			logger.info(e.getMessage(), e);
			return null;
		}
	}

	private void addNewClient(SocketChannel channel) {
		BlockingQueue<IoBuffer> q = new LinkedBlockingQueue<IoBuffer>();
		try {
			final Transport transport = this.ioFactory.createTransport(channel, q);
			transport.addCloseListener(new TransportCloseListener() {
				public void onClosed(Transport transport) {
					queueMapper.remove(transport.getId());
					transportMapper.remove(transport.getId());
				}
			});

			channel.configureBlocking(false);
			channel.register(this.readSelector.selector(),
					SelectionKey.OP_READ, new StringBuffer());
			try {
				this.acceptQ.put(transport);
				this.queueMapper.put(transport.getId(), q);
				this.transportMapper.put(transport.getId(), transport);
			} catch(InterruptedException e) {
				logger.info(e.getMessage(), e);
				channel.close();
				throw new RuntimeException(e);
			}
		} catch (ClosedChannelException cce) {
			logger.error(cce.getMessage(), cce);
			throw new RuntimeException(cce);
		} catch (IOException ioe) {
			logger.error(ioe.getMessage(), ioe);
			throw new RuntimeException(ioe);
		}
	}

	public void run() {
		SocketChannel clientChannel;
		try {
			synchronized(serverSocketChannel) {
				if (serverSocketChannel.isOpen()) {
					if ((clientChannel = this.serverSocketChannel.accept()) != null) {
						logger.debug("accepted channel");
						addNewClient(clientChannel);
					}
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	public void close() {
		runner.stop();
		synchronized(serverSocketChannel) {
			if (serverSocketChannel != null && serverSocketChannel.isOpen()) {
				try {
					serverSocketChannel.close();
				} catch(IOException e) {
					logger.error(e.getMessage(), e);
					throw new NIOException(e);
				}
			}
		}
	}
}
