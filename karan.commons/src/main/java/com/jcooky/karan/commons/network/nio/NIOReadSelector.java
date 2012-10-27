package com.jcooky.karan.commons.network.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcooky.karan.commons.network.AbstractIOFactory;
import com.jcooky.karan.commons.network.buffer.IoBuffer;
import com.jcooky.karan.commons.network.listeners.ChannelCloseListener;

public class NIOReadSelector implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(NIOReadSelector.class);
	
	private static final int DEFAULT_BUFFER_SIZE = 1024;
	
	private Map<String, BlockingQueue<IoBuffer>> readQ;
	private Selector readSelector;
	private IoBuffer readBuffer;
	private boolean running = true;
	private Set<ChannelCloseListener> forceCloseListeners = new HashSet<ChannelCloseListener>();
	private AbstractIOFactory ioFactory;
	
	public NIOReadSelector(AbstractIOFactory ioFactory, Selector readSelector, Map<String, BlockingQueue<IoBuffer>> q) {
		this.readSelector = readSelector;
		this.readQ = q;
		this.readBuffer = IoBuffer.allocate(DEFAULT_BUFFER_SIZE, true);
		this.ioFactory = ioFactory;
	}
	
	public Selector selector() {
		return this.readSelector;
	}
	
	public void addForceCloseListener(ChannelCloseListener listener) {
		forceCloseListeners.add(listener);
	}
	
	public void triggerOnClosed(String channelId) {
		for (ChannelCloseListener listener : forceCloseListeners) {
			listener.onClosed(channelId);
		}
	}
	
	public boolean isClosed() {
		return !running;
	}
	
	public void run() {
		try {
			this.readSelector.selectNow();

			Set<SelectionKey> readyKeys = this.readSelector.selectedKeys();

			Iterator<SelectionKey> i = readyKeys.iterator();
			while (i.hasNext()) {
				SelectionKey key = i.next();
				i.remove();
				SocketChannel channel = (SocketChannel) key.channel();
				this.readBuffer.clear();

				int nBytes;
				IoBuffer readedBuffer = ioFactory.createIoBuffer(DEFAULT_BUFFER_SIZE);
				readedBuffer.setAutoExpand(true);
				readedBuffer.clear();
				while((nBytes = channel.read(this.readBuffer.buf())) != 0) {
					if (nBytes == -1) {
						logger.debug("disconnected form server: end-of-stream");
						channel.close();
						triggerOnClosed((String)key.attachment());
						break;
					} else if (nBytes > 0) {
						logger.debug("recv bytes : {}", nBytes);
						this.readBuffer.flip();
						readedBuffer.put(this.readBuffer);
						this.readBuffer.clear();
					}
				}
				readedBuffer.flip();
				logger.debug("finished recv total bytes : {}", readedBuffer.remaining());
				logger.debug("recv bytes hexdump : {}", readedBuffer.getHexDump());
				String qKey = (String)key.attachment();
				logger.debug("readQ put attempt qKey: {}", qKey);
				if (readQ.containsKey(qKey)) {
					readQ.get(qKey).put(readedBuffer);
				}
			}
		} catch (IOException ioe) {

		} catch (Exception e) {

		}
		
	}
}
