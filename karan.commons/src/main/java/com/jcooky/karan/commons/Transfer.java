package com.jcooky.karan.commons;

public abstract class Transfer<T> {
	public abstract void send(T data);
	public abstract T recv();
	public abstract void close();
}
