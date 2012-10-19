package com.jcooky.karan.commons.nio;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.net.InetAddress;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcooky.karan.commons.network.AbstractIOFactory;
import com.jcooky.karan.commons.network.Acceptor;
import com.jcooky.karan.commons.network.Connector;
import com.jcooky.karan.commons.network.Transport;
import com.jcooky.karan.commons.network.buffer.IoBuffer;
import com.jcooky.karan.commons.network.nio.NIOFactory;
import com.jcooky.karan.commons.test.utils.R;

public class NIOTransportTest {
	private static final Logger logger = LoggerFactory.getLogger(NIOTransportTest.class);
	private static final int PORT = 8021;
	
	private AbstractIOFactory ioFactory ;
	private Acceptor acceptor;
	private Connector connector;
	
	private Transport serverTransport, clientTransport;
	
	@Before
	public void setUp() {
		ioFactory = spy(NIOFactory.get());
		acceptor = spy(ioFactory.createAcceptor());
		connector = spy(ioFactory.createConnector());
	}
	
	@After
	public void tearDown() throws Exception {
		acceptor.close();
		Thread.sleep(50);
		
		verify(ioFactory, times(1)).createAcceptor();
		verify(ioFactory, times(1)).createConnector();
	}
	
	@Test
	public void testRecvAndSend() throws Exception {
		final IoBuffer sendData = IoBuffer.wrap(IOUtils.toByteArray(R.text));
		acceptor.setIdleTime(1);
		acceptor.listen(PORT);
		
		Thread thread = new Thread() {
			public void run() {
				try {
					while((serverTransport = acceptor.accept()) == null) {
//						logger.debug("non accept");
						Thread.sleep(1);
					}
					serverTransport = spy(serverTransport);
					logger.debug("serverTransport is accepted : {}", serverTransport.getId());
					
					serverTransport.send(sendData);
					sendData.free();
				} catch(Exception e) {
					throw new RuntimeException();
				}
			}
		};
		
		thread.start();
		Thread.sleep(1);
		clientTransport = spy(connector.connect(InetAddress.getByName("localhost"), PORT));
		IoBuffer recvBuf = null;
		while((recvBuf = clientTransport.recv()) == null) {
//			logger.debug("non recv");
			Thread.sleep(1);
		}
		recvBuf.free();
		assertEquals(IOUtils.toString(R.text), IOUtils.toString(recvBuf.asInputStream()));
		
		clientTransport.close();
		serverTransport.close();
		
		verify(serverTransport, times(1)).close();
		verify(clientTransport, times(1)).close();
		verify(serverTransport, times(1)).send(sendData);
		verify(serverTransport, times(1)).getId();
		verify(acceptor, times(1)).listen(PORT);
	}
	
}
