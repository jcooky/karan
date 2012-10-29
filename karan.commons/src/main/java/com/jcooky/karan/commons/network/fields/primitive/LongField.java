package com.jcooky.karan.commons.network.fields.primitive;

import com.jcooky.karan.commons.network.buffer.IoBuffer;
import com.jcooky.karan.commons.network.field.FieldCode;
import com.jcooky.karan.commons.network.fields.AbstractField;
import com.jcooky.karan.commons.network.fields.Field;
import com.jcooky.karan.commons.util.ByteUtils;

public class LongField extends AbstractField<Long> {
	
	public LongField(Field<?> prev) {
		super(prev);
	}

	public IoBuffer toBytes() {
		byte []bytes = new byte[length()];
		bytes[0] = FieldCode.Long.code();
		ByteUtils.toBytes(get(), bytes, 1);
		
		return IoBuffer.wrap(bytes);
	}
	
	public void fromBytes(IoBuffer bytes) {
		if (!FieldCode.BYTE.equals(bytes.get())) {
			throw new ByteDecodingException();
		}
		
		long qword = 0;
		for (int i = 0; i < 8; i++) {
			qword = (qword << 8) + (bytes.get() & 0xFF);
		}
		set(qword);
	}

	public int length() {
		return Long.SIZE + Byte.SIZE;
	}

}
