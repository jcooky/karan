package com.jcooky.karan.commons.network;

import com.jcooky.karan.commons.network.buffer.IoBuffer;
import com.jcooky.karan.commons.network.fields.Field;

public abstract class AbstractSpec implements Spec {
	private Field<?> head;
	
	protected void setHead(Field<?> head) {
		this.head = head;
	}
	
	public abstract byte getType();
	
	public void toBytes(IoBuffer buffer) {
		buffer.put(getType());
		
		for (Field<?> field = head; field != null; field = field.next()) {
			buffer.put(field.toBytes());
		}
	}
	
	public void build(IoBuffer ioBuffer) {
		for (Field<?> field = head ; field != null ; field = field.next()) {
			field.fromBytes(ioBuffer);
		}
	}
}
