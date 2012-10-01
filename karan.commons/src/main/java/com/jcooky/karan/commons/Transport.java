package com.jcooky.karan.commons;

import com.jcooky.karan.commons.buffer.IoBuffer;
import com.jcooky.karan.commons.listeners.TransportCloseListener;

public interface Transport {
	public class LackBufferException extends RuntimeException {
		
	}
	public void addCloseListener(TransportCloseListener cl);
	public String getId();
	
	public void setWriteBufferSize(int size);
	public int getWriteBufferSize();
	
	public IoBuffer recv();
	public void send(IoBuffer bytes);
	
	public boolean isClosed();
	public void close();
}
