package com.jcooky.karan.commons.nio;

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;

import com.jcooky.karan.commons.AbstractIOFactory;
import com.jcooky.karan.commons.Session;
import com.jcooky.karan.commons.Transport;
import com.jcooky.karan.commons.buffer.IoBuffer;

public class NIOSession implements Session {
	
	private Transport transport;
	private AbstractIOFactory ioFactory;
	
	public NIOSession(AbstractIOFactory ioFactory, Transport transport) {
		this.transport = transport;
		this.ioFactory = ioFactory;
	}

	public IoBuffer recv() {
		IoBuffer buf = transport.recv();
		if (buf.get() == 0) {
			return recv();
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
		transport.close();
	}



}
