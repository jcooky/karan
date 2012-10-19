package com.jcooky.karan.commons.network.nio;

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;

import com.jcooky.karan.commons.network.AbstractIOFactory;
import com.jcooky.karan.commons.network.Session;
import com.jcooky.karan.commons.network.Transport;
import com.jcooky.karan.commons.network.buffer.IoBuffer;

public class NIOSession implements Session {
	
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
			if (buf.get() == 0) {
				return recv();
			}
		}
		
		return buf;
	}

	public void send(IoBuffer bytes) {
		if (!isOnline()) {
			throw new RefusedConnectionException();
		}
		try {
			bytes.put(0, (byte)1);
			transport.send(bytes);
		} catch(RuntimeException e) {
			if (e.getCause() instanceof ClosedChannelException) {
				throw new RefusedConnectionException(e.getCause());
			}
		}
	}
	
	private void ping() {
		IoBuffer buf = ioFactory.createIoBuffer(1);
		buf.put((byte)1);
		
		transport.send(buf);
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
