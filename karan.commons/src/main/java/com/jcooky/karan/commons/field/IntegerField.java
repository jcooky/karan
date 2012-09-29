package com.jcooky.karan.commons.field;

import com.jcooky.karan.commons.Field;
import com.jcooky.karan.commons.buffer.IoBuffer;
import com.jcooky.karan.commons.util.ByteUtils;

public class IntegerField extends Field {
	
	private Integer data;
	
	public IntegerField(Field prev) {
		super(prev);
	}
	
	public IntegerField(Integer data, Field prev) {
		super(prev);
		
		this.data = data;
	}
	
	@Override
	public Class<?> getPrimitiveClass() {
		return Integer.class;
	}

	@Override
	public IoBuffer toBytes() {
		byte[] bytes = new byte[1 + Integer.SIZE/Byte.SIZE];
		bytes[0] = FieldCode.INTEGER.code();
		ByteUtils.toBytes(data, bytes, 1);
		
		return IoBuffer.wrap(bytes);
	}
	
	@Override
	public void fromBytes(IoBuffer bytes) {
		if (!FieldCode.INTEGER.equals(bytes.get())) {
			throw new ByteDecodingException();
		}
		
		int dword = 0;
		for (int i = 0; i < 4; i++) {
			dword = (dword << 8) + (bytes.get() & 0xFF);
		}
		data = dword;
	}

	@Override
	public int getLength() {
		return Integer.SIZE;
	}

	@Override
	public Object getData() {
		return this.data;
	}

	@Override
	public void setData(Object data) {
		if (data instanceof Integer) {
			this.data = (Integer)data;
		} else {
			throw new TypeMatchException();
		}
	}
	
}
