package com.jcooky.karan.commons.network;

import com.jcooky.karan.commons.network.buffer.IoBuffer;
import com.jcooky.karan.commons.network.listeners.TransportCloseListener;

/**
 * 
 * @author jcooky
 * 
 */
public interface Transport {
	public class LackBufferException extends RuntimeException {
		
	}
	public void addCloseListener(TransportCloseListener cl);
	public String getId();
	/**
	 * 
	 * @return IoBuffer recvBuffer returned
	 */
	public IoBuffer recv();
	public void send(IoBuffer bytes);
	
	public boolean isClosed();
	public void close();
}
