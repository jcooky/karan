package com.jcooky.karan.commons;

import com.jcooky.karan.commons.buffer.IoBuffer;

public interface Spec {
	public int getType();
	public Field getHeadField();
	public abstract void build(IoBuffer ioBuffer);
}
