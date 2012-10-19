package com.jcooky.karan.commons.network.nio;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcooky.karan.commons.network.AbstractIOFactory;
import com.jcooky.karan.commons.network.AbstractTransport;
import com.jcooky.karan.commons.network.buffer.IoBuffer;

public class NIOTransport extends AbstractTransport {
	private static final Logger logger = LoggerFactory.getLogger(NIOTransport.class);
	
	private static long CHANNEL_WRITE_SLEEP = 1L;
	
	private SocketChannel socketChannel;
	private BlockingQueue<IoBuffer> readQ;
	private boolean closed = false;
	
	public NIOTransport(AbstractIOFactory ioFactory, SocketChannel socketChannel, BlockingQueue<IoBuffer> q) {
		this.socketChannel = socketChannel;
		this.readQ = q;
	}
	
	public IoBuffer recv() {
		try {
			if (!readQ.isEmpty()) {
				return readQ.take();
			}
		} catch (InterruptedException e) {
		}
		
		return null;
	}

	public void send(IoBuffer bytes) {
		long nBytes = 0;
		long toWrite = bytes.remaining();

		try {
			while (nBytes != toWrite) {
				int b = socketChannel.write(bytes.buf());
				nBytes += b;
				logger.debug("sending data : {}B", b);

				try {
					Thread.sleep(CHANNEL_WRITE_SLEEP);
				} catch (InterruptedException e) {

				}
			}
			logger.debug("finish sending data : {}B", nBytes);
		} catch (ClosedChannelException e) {
			logger.error(e.getMessage(), e);
			throw new NIOException(e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new NIOException(e);
		} finally {
			
		}
		bytes.rewind();
	}
	
	public boolean isClosed() {
		return closed;
	}

	public void close() {
		if(!isClosed()) {
			return ;
		}
		try {
			closed = true;
			if (!socketChannel.isConnected()) {
				socketChannel.close();
			}
			
			super.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	
}
