package com.jcooky.karan.commons;

import java.nio.ByteBuffer;

import org.junit.Before;
import org.junit.Test;

public class ByteBufferTest {
	
	@Before
	public void setUp() {
		
	}
	
	@Test
	public void testPutOverCapacity() {
		ByteBuffer buf = ByteBuffer.allocate(Integer.SIZE/8);
		buf.putInt(4);
		
		buf.putInt(8);
		
		System.out.println(buf.capacity());
	}
}
