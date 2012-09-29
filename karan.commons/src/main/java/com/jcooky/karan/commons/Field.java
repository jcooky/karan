package com.jcooky.karan.commons;

import com.jcooky.karan.commons.buffer.IoBuffer;

/**
 * 데이터를 표현하는 최소 단위.
 * 
 * @author jcooky
 *
 */
public abstract class Field {
	public static class TypeMatchException extends RuntimeException {
		
	}
	
	public static class ByteDecodingException extends RuntimeException {
		
	}
	
	private Field prev = null;
	private Field next = null;
	
	public Field(Field prev) {
		this.prev = prev;
		prev.next = this;
	}
	
	public abstract IoBuffer toBytes();
	public abstract void fromBytes(IoBuffer bytes) throws ByteDecodingException;
	public abstract int getLength();
	public abstract Object getData();
	public abstract void setData(Object data) throws TypeMatchException;
	public abstract Class<?> getPrimitiveClass();
	
	public int getPos() {
		int pos = 0;
		if (prev != null) {
			pos += prev.getPos();
		}
		
		return pos + getLength();
	}
	
	public Field getNext() {
		return next;
	}
	
	public Field getPrev() {
		return prev;
	}
}
