package com.jcooky.karan.commons.network;

public interface Transfer<T> {
	public void send(T data);
	public T recv();
	public void close();
}
