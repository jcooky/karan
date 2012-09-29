package com.jcooky.karan.commons;

import com.jcooky.karan.commons.buffer.IoBuffer;
import com.jcooky.karan.commons.listeners.CloseListener;

public interface Transport {
	public class LackBufferException extends RuntimeException {
		
	}
	public void addCloseListener(CloseListener cl);
	public String getId();
	
	public void setWriteBufferSize(int size);
	public int getWriteBufferSize();
	
	public IoBuffer recv();
	public void send(IoBuffer bytes);
	
	public void close();
}
