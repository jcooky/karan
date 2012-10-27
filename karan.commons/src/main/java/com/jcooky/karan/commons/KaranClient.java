package com.jcooky.karan.commons;

public interface KaranClient {
	public <T> T get(String name, Class<T> typeOfInterface);
	public void connect(String host, int port);
}
