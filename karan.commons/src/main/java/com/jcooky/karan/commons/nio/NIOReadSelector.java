package com.jcooky.karan.commons.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import com.jcooky.karan.commons.AbstractIOFactory;
import com.jcooky.karan.commons.buffer.IoBuffer;
import com.jcooky.karan.commons.listeners.ChannelCloseListener;

public class NIOReadSelector implements Runnable {
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
	
	public void close() {
		running = false;
	}
	
	public void run() {
		while(running) {
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
							System.out
									.println("disconnected form server: end-of-stream");
							channel.close();
							triggerOnClosed((String)key.attachment());
							break;
						} else if (nBytes > 0) {
	
							this.readBuffer.flip();
							readedBuffer.put(this.readBuffer.buf());
							this.readBuffer.clear();
						}
					}
					readedBuffer.flip();

					String qKey = (String)key.attachment();
					if (readQ.containsKey(qKey)) {
						readQ.get(qKey).put(readedBuffer);
					}
				}
			} catch (IOException ioe) {

			} catch (Exception e) {

			}
			
//			try {
//				Thread.sleep(50);
//			} catch(InterruptedException e) {
//			}
		}
		
	}
}
