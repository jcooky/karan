package com.jcooky.karan.commons;

import java.nio.ByteBuffer;

import com.jcooky.karan.commons.buffer.IoBuffer;


public interface Session {
	public class RefusedConnectionException extends RuntimeException {
		public RefusedConnectionException() {
			super();
		}
		
		public RefusedConnectionException(Throwable e) {
			super(e);
		}
	}
	
	public IoBuffer recv() throws RefusedConnectionException ;
	public void send(IoBuffer bytes) throws RefusedConnectionException ;
	public boolean isOnline();
	public void close() throws RefusedConnectionException ;
}
