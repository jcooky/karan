package com.jcooky.karan.commons.nio;

public class NIOException extends RuntimeException {
	public NIOException() {
		super();
	}
	
	public NIOException(String msg) {
		super(msg);
	}
	
	public NIOException(Throwable e) {
		super(e);
	}
	
	public NIOException(String msg, Throwable e) {
		super(msg, e);
	}
}
