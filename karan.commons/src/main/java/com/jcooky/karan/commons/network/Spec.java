package com.jcooky.karan.commons.network;

import com.jcooky.karan.commons.network.buffer.IoBuffer;
import com.jcooky.karan.commons.network.fields.Field;

public interface Spec {
	public int getType();
	public IoBuffer toBytes();
	public abstract void build(IoBuffer ioBuffer);
}
