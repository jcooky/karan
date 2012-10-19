package com.jcooky.karan.commons.nio;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.jcooky.karan.commons.network.AbstractIOFactory;
import com.jcooky.karan.commons.network.Acceptor;
import com.jcooky.karan.commons.network.Connector;
import com.jcooky.karan.commons.network.Session;
import com.jcooky.karan.commons.network.Transfer;
import com.jcooky.karan.commons.network.Transport;
import com.jcooky.karan.commons.network.buffer.IoBuffer;
import com.jcooky.karan.commons.network.nio.NIOReadSelector;
import com.jcooky.karan.commons.test.utils.Counter;

public class NIOReadSelectorTest {
	
	public static class StubIOFactory extends AbstractIOFactory {
		
		private List<IoBuffer> ioBuffers = new ArrayList<IoBuffer>();
		
		public List<IoBuffer> getIoBuffers() {
			return ioBuffers;
		}
		
		public Acceptor createAcceptor() {
			// TODO Auto-generated method stub
			return null;
		}

		public Connector createConnector() {
			// TODO Auto-generated method stub
			return null;
		}

		public Session createSession(Transport transport) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T> Transfer<T> createTransfer(Session session) {
			// TODO Auto-generated method stub
			return null;
		}

		public <T> Transfer<T> createTransfer(Transport transport) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Transport createTransport(SocketChannel socketChannel,
				BlockingQueue<IoBuffer> readQ) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IoBuffer createIoBuffer() {
			return IoBuffer.allocate(1024);
		}

		@Override
		public IoBuffer createIoBuffer(int capacity) {
			IoBuffer ioBuffer = Mockito.spy(IoBuffer.allocate(capacity, true));
			ioBuffers.add(ioBuffer);
			
			return ioBuffer;
		}
		
	}
	
	static {
		AbstractIOFactory.register("Stub", StubIOFactory.class);
	}
	
	private @Mock Selector mockSelector;
	private @Spy BlockingQueue<IoBuffer> q = new LinkedBlockingQueue<IoBuffer>();
	private @Mock Set<SelectionKey> mockKeys;
	private @Mock Iterator<SelectionKey> mockIterator;
	private @Mock SelectionKey mockKey ;
	private @Mock SocketChannel mockSc;
	
	private CharsetDecoder decoder = Charset.forName("US-ASCII").newDecoder();
	
	private boolean resultHasNext = true;
	private NIOReadSelector readSelector;
	
	private AbstractIOFactory ioFactory = AbstractIOFactory.get("Stub");
	
	@Before
	public void setUp() {
		Map<String, BlockingQueue<IoBuffer>> qMapper = new HashMap<String, BlockingQueue<IoBuffer>>();
		qMapper.put(UUID.randomUUID().toString(), q);
		MockitoAnnotations.initMocks(this);
		
		Mockito.when(mockSelector.selectedKeys()).thenReturn(mockKeys);
		Mockito.when(mockIterator.hasNext()).thenAnswer(new Answer<Boolean>() {

			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				return resultHasNext;
			}
			
		});
		Mockito.when(mockKeys.iterator()).thenReturn(mockIterator);
		Mockito.when(mockIterator.next()).thenReturn(mockKey);
		Mockito.when(mockKey.channel()).thenReturn(mockSc);
		
		readSelector = new NIOReadSelector(ioFactory, mockSelector, qMapper);
	}
	
	@Test
	public void testSelect() throws Exception {
		final Counter callCounter = new Counter(0);
		Mockito.when(mockSc.read(Mockito.any(ByteBuffer.class))).thenAnswer(new Answer<Integer>() {

			public Integer answer(InvocationOnMock invocation) throws Throwable {
				synchronized(callCounter) {
					
					if (callCounter.count() >= 1) {
						resultHasNext = false;
						return 0;
					} else {
						callCounter.increment();
					}
				}
				byte []retBytes = "Testing".getBytes("UTF-8");
				Object []args = invocation.getArguments();
				
				ByteBuffer bufArg = (ByteBuffer)args[0];
				bufArg.put(retBytes);
				
				return retBytes.length;
			}
			
		});
		
		Thread thread = Executors.defaultThreadFactory().newThread(readSelector);
		thread.start();
		thread.join();
		
		IoBuffer ioBuffer = q.take();
		Assert.assertNotNull(ioBuffer);
		Assert.assertNotSame(0, ioBuffer.remaining());
		String str = ioBuffer.getString(decoder);
		ioBuffer.free();
		
		Assert.assertEquals("Testing", str);
	}
}
