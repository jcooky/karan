package com.github.karan.exception;

public class KaranException extends RuntimeException {

	public KaranException() {
		super();
	}

	public KaranException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public KaranException(String arg0) {
		super(arg0);
	}

	public KaranException(Throwable arg0) {
		super(arg0);
	}
	
}
