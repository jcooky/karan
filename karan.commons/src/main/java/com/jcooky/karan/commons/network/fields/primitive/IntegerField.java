package com.jcooky.karan.commons.network.fields.primitive;

import com.jcooky.karan.commons.network.buffer.IoBuffer;
import com.jcooky.karan.commons.network.field.FieldCode;
import com.jcooky.karan.commons.network.fields.AbstractField;
import com.jcooky.karan.commons.network.fields.Field;
import com.jcooky.karan.commons.util.ByteUtils;

public class IntegerField extends AbstractField<Integer> {
	
	public IntegerField(Field prev) {
		super(prev);
	}
	
	public IoBuffer toBytes() {
		byte[] bytes = new byte[1 + Integer.SIZE/Byte.SIZE];
		bytes[0] = FieldCode.INTEGER.code();
		ByteUtils.toBytes(get(), bytes, 1);
		
		return IoBuffer.wrap(bytes);
	}
	
	public void fromBytes(IoBuffer bytes) {
		if (!FieldCode.INTEGER.equals(bytes.get())) {
			throw new ByteDecodingException();
		}
		
		int dword = 0;
		for (int i = 0; i < 4; i++) {
			dword = (dword << 8) + (bytes.get() & 0xFF);
		}
		set(dword);
	}

	public int length() {
		return Integer.SIZE + 1;
	}

}
