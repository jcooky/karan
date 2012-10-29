package com.jcooky.karan.commons.network;

import com.jcooky.karan.commons.network.buffer.IoBuffer;
import com.jcooky.karan.commons.network.fields.Field;

public interface Spec {
	public byte getType();
	public void toBytes(IoBuffer ioBuffer);
	public abstract void build(IoBuffer ioBuffer);
}
