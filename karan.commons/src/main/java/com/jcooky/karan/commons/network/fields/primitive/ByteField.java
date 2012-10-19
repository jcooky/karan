package com.jcooky.karan.commons.network.fields.primitive;

import com.jcooky.karan.commons.network.buffer.IoBuffer;
import com.jcooky.karan.commons.network.field.FieldCode;
import com.jcooky.karan.commons.network.fields.AbstractField;
import com.jcooky.karan.commons.network.fields.Field;

public class ByteField extends AbstractField<Byte> {
	private Byte data;
	
	public ByteField(Field prev) {
		super(prev);
	}

	public IoBuffer toBytes() {
		return IoBuffer.wrap(new byte[]{FieldCode.BYTE.code(), data});
	}
	
	public void fromBytes(IoBuffer bytes) {
		if (!FieldCode.BYTE.equals(bytes.get())) {
			throw new ByteDecodingException();
		}
		
		data = bytes.get();
	}

	public int length() {
		return 2;
	}

}
