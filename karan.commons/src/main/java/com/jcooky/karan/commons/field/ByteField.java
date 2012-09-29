package com.jcooky.karan.commons.field;

import com.jcooky.karan.commons.Field;
import com.jcooky.karan.commons.buffer.IoBuffer;

public class ByteField extends Field {
	private Byte data;
	
	public ByteField(Field prev) {
		super(prev);
	}

	@Override
	public IoBuffer toBytes() {
		return IoBuffer.wrap(new byte[]{FieldCode.BYTE.code(), data});
	}
	
	@Override
	public void fromBytes(IoBuffer bytes) {
		if (!FieldCode.BYTE.equals(bytes.get())) {
			throw new ByteDecodingException();
		}
		
		data = bytes.get();
	}

	@Override
	public int getLength() {
		return 1;
	}

	@Override
	public Object getData() {
		return data;
	}

	@Override
	public void setData(Object data) {
		this.data = (Byte)data;
	}

	@Override
	public Class<?> getPrimitiveClass() {
		return Byte.class;
	}
}
