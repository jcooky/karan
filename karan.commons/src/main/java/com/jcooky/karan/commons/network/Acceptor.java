package com.jcooky.karan.commons.network;


public interface Acceptor {
	public Transport accept();
	public void setIdleTime(int idleTime);
	public void listen(int port);
	public void close();
}
