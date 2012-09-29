package com.jcooky.karan.commons.field;

import java.io.UnsupportedEncodingException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcooky.karan.commons.Field;
import com.jcooky.karan.commons.buffer.IoBuffer;

public class StringField extends Field {
	private static final CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
	
	private Logger logger = LoggerFactory.getLogger(StringField.class);
	private String data;
	
	public StringField(Field prev) {
		super(prev);
	}

	@Override
	public IoBuffer toBytes() {
		try {
			return IoBuffer.wrap(data.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void fromBytes(IoBuffer bytes) throws ByteDecodingException {
		if (!FieldCode.STRING.equals(bytes.get())) {
			throw new ByteDecodingException();
		}
		
		int len = len(bytes.get());
		byte[] bs = new byte[len];
		bytes.get(bs);
		try {
			data = new String(bs, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	private int len(byte b) {
		return b < 0 ? 127 - b : b;
	}

	@Override
	public int getLength() {
		try {
			return data.getBytes("UTF-8").length + 1;
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object getData() {
		return data;
	}

	@Override
	public void setData(Object data) {
		this.data = (String)data;
	}

	@Override
	public Class<?> getPrimitiveClass() {
		return String.class;
	}
	
	
}
