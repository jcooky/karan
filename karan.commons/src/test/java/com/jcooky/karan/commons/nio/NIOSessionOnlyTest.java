package com.jcooky.karan.commons.nio;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcooky.karan.commons.network.AbstractIOFactory;
import com.jcooky.karan.commons.network.Session;
import com.jcooky.karan.commons.network.Transport;
import com.jcooky.karan.commons.network.buffer.IoBuffer;
import com.jcooky.karan.commons.network.nio.NIOFactory;
import com.jcooky.karan.commons.network.nio.NIOSession;
import com.jcooky.karan.commons.test.utils.R;

public class NIOSessionOnlyTest {
	private static final Logger logger = LoggerFactory.getLogger(NIOSessionOnlyTest.class);
	
	private AbstractIOFactory ioFactory = NIOFactory.get();	
	private Transport mockTransport;
	private Session session ;
	private IoBuffer sendData;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		sendData = IoBuffer.wrap(IOUtils.toByteArray(R.text));
		
		mockTransport = mock(Transport.class);
		session = spy(new NIOSession(ioFactory, mockTransport));
		
		when(mockTransport.recv()).thenReturn(sendData);
	}
	
	@After
	public void tearDown() throws Exception {
		sendData.free();
		session.close();
		
		verify(mockTransport, times(1)).close();
	}
	
	@Test
	public void testRecv() throws Exception {
		IoBuffer data = session.recv();
		logger.debug("recv data : {}", IOUtils.toString(data.asInputStream()));
		
		assertEquals(sendData, data);
		
		verify(session, times(1)).recv();
		verify(mockTransport, times(1)).recv();
	}
}
