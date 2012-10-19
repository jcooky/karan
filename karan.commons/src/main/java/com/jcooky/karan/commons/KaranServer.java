package com.jcooky.karan.commons;

public interface KaranServer {
	public <T> void addService(String name, T serviceImpl);
	public void listen(int port);
}
