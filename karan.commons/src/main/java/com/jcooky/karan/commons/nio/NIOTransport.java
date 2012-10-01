package com.jcooky.karan.commons.nio;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcooky.karan.commons.AbstractIOFactory;
import com.jcooky.karan.commons.AbstractTransport;
import com.jcooky.karan.commons.buffer.IoBuffer;

public class NIOTransport extends AbstractTransport {
	private static final Logger logger = LoggerFactory.getLogger(NIOTransport.class);
	
	private static long CHANNEL_WRITE_SLEEP = 1L;
	
	private IoBuffer writeBuffer;
	
	private SocketChannel socketChannel;
	private BlockingQueue<IoBuffer> readQ;
	private boolean closed = false;
	
	public NIOTransport(AbstractIOFactory ioFactory, SocketChannel socketChannel, BlockingQueue<IoBuffer> q) {
		this.socketChannel = socketChannel;
		this.readQ = q;
		this.writeBuffer = ioFactory.createIoBuffer(1024);
		this.writeBuffer.setAutoExpand(true);
	}
	
	public void setWriteBufferSize(int size) {
		writeBuffer.capacity(size);
	}
	
	public int getWriteBufferSize() {
		return this.writeBuffer.capacity();
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
		this.writeBuffer.clear();
		this.writeBuffer.put(bytes);
		this.writeBuffer.flip();
		 
		long nBytes = 0;
		long toWrite = writeBuffer.remaining();

		try {
			while (nBytes != toWrite) {
				nBytes += socketChannel.write(writeBuffer.buf());

				try {
					Thread.sleep(CHANNEL_WRITE_SLEEP);
				} catch (InterruptedException e) {

				}
			}
		} catch (ClosedChannelException e) {
			logger.error(e.getMessage(), e);
			throw new NIOException(e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new NIOException(e);
		} finally {
			
		}
		writeBuffer.rewind();
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
			
			this.writeBuffer.free();
			
			super.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	
}
