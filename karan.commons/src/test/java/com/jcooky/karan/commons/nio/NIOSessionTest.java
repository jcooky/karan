package com.jcooky.karan.commons.nio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcooky.karan.commons.network.AbstractIOFactory;
import com.jcooky.karan.commons.network.Acceptor;
import com.jcooky.karan.commons.network.Connector;
import com.jcooky.karan.commons.network.Session;
import com.jcooky.karan.commons.network.Transport;
import com.jcooky.karan.commons.network.buffer.IoBuffer;
import com.jcooky.karan.commons.network.nio.NIOFactory;
import com.jcooky.karan.commons.test.utils.R;

@RunWith(MockitoJUnitRunner.class)
public class NIOSessionTest {
	private static final Logger logger = LoggerFactory.getLogger(NIOSessionTest.class);
	
	private AbstractIOFactory ioFactory = NIOFactory.get();
	private ExecutorService threadPool = Executors.newCachedThreadPool();
	private @Spy Acceptor acceptor = ioFactory.createAcceptor();
	private @Spy Connector connector = ioFactory.createConnector();
	
	@Before
	public void setUp() {
		acceptor.listen(8080);
	}
	
	@Test
	public void testSession() throws Exception {
		String text = IOUtils.toString(R.text, "UTF-8");
		threadPool.execute(new Runnable() {
			public void run() {
				try {
					Transport transport = null;
					while((transport = acceptor.accept()) == null);
					
					Session session = ioFactory.createSession(transport);
					session.send(IoBuffer.wrap(IOUtils.toByteArray(R.text)));
					session.close();
				} catch(Exception e) {
					throw new RuntimeException();
				}
			}
		});
		
		Transport transport = connector.connect(InetAddress.getLocalHost(), 8080);
		Session session = ioFactory.createSession(transport);
		IoBuffer buf = null;
		while ((buf = session.recv()) == null);
		assertNotNull(buf);
		
		String recvText = IOUtils.toString(buf.asInputStream(), "UTF-8");
		logger.debug(IoBuffer.wrap(IOUtils.toByteArray(R.text)).getHexDump());
		
		assertEquals(text, recvText);
	}
}
