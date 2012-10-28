package com.jcooky.karan.commons.network.nio;

import java.nio.channels.ClosedChannelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcooky.karan.commons.network.AbstractIOFactory;
import com.jcooky.karan.commons.network.Session;
import com.jcooky.karan.commons.network.Transport;
import com.jcooky.karan.commons.network.buffer.IoBuffer;

public class NIOSession implements Session {
	private static final Logger logger = LoggerFactory.getLogger(NIOSession.class);
	
	private Transport transport;
	private AbstractIOFactory ioFactory;
	private IoBuffer remainingBuffer = null;
	
	public NIOSession(AbstractIOFactory ioFactory, Transport transport) {
		this.transport = transport;
		this.ioFactory = ioFactory;
	}

	public IoBuffer recv() {
		IoBuffer buf ;
		if (remainingBuffer == null || !remainingBuffer.hasRemaining()) {
			if (remainingBuffer != null) {
				remainingBuffer.free();
			}
			remainingBuffer = transport.recv();
		}
		buf = remainingBuffer;
		
		if (buf != null) {
			switch(buf.get()) {
			case 0:
				return recv();
			case 1:
				break;
			}
		}
		
		return buf;
	}

	public void send(IoBuffer bytes) {
		if (!isOnline()) {
			throw new RefusedConnectionException();
		}
		IoBuffer buf = null;
		try {
			buf = ioFactory.createIoBuffer(bytes.capacity() + 1);
			buf.put((byte)1);
			buf.put(bytes);
			buf.flip();
			transport.send(buf);
			
		} catch(RuntimeException e) {
			if (e.getCause() instanceof ClosedChannelException) {
				throw new RefusedConnectionException(e.getCause());
			}
		} finally {
			if (buf != null) {
				buf.free();
			}
		}
	}
	
	private void ping() {
		IoBuffer buf = null; 
		
		try {
			logger.debug("send ping!");
			
			buf = ioFactory.createIoBuffer(1);
			buf.put((byte)0);
			buf.flip();
			transport.send(buf);
		} finally {
			if (buf != null) {
				buf.free();
			}
		}
	}

	public boolean isOnline() {
		try {
			ping();
			return true;
		} catch (RuntimeException e) {
			return false;			
		}
		
	}

	public void close() {
		if (remainingBuffer != null) {
			remainingBuffer.free();
		}
		transport.close();
	}



}
