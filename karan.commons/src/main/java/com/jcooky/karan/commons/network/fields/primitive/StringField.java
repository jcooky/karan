package com.jcooky.karan.commons.network.fields.primitive;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcooky.karan.commons.network.buffer.IoBuffer;
import com.jcooky.karan.commons.network.field.FieldCode;
import com.jcooky.karan.commons.network.fields.AbstractField;
import com.jcooky.karan.commons.network.fields.Field;

public class StringField extends AbstractField<String> {
	
	public StringField(Field prev) {
		super(prev);
	}

	private static final Logger logger = LoggerFactory.getLogger(StringField.class);
	
	public IoBuffer toBytes() {
		byte[] bytes;
		try {
			bytes = get().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		IoBuffer buf = IoBuffer.allocate(length());
		buf.put(FieldCode.STRING.code());
		buf.put((byte)bytes.length);
		buf.put(bytes);
		return buf;
	}

	public void fromBytes(IoBuffer buf) {
		if (buf.get() == FieldCode.STRING.code()) {
			throw new ByteDecodingException();
		}
		int len = (int)buf.get();
		len = len < 0 ? 256 + len : len;
		
		byte[] bytes = new byte[len];
		buf.get(bytes);
		
		try {
			set(new String(bytes, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	public int length() {
		try {
			return get().getBytes("UTF-8").length + Character.SIZE + Byte.SIZE;
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
