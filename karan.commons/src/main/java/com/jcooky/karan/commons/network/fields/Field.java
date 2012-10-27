package com.jcooky.karan.commons.network.fields;

import com.jcooky.karan.commons.network.buffer.IoBuffer;

public interface Field<T> {
	public class ByteDecodingException extends RuntimeException {
		
	}
	public IoBuffer toBytes();
	public void fromBytes(IoBuffer buf);
	public int length();
	public T get();
	public void set(T val);
	public <U> Field<U> next();
	public <U> Field<U> prev();
}
