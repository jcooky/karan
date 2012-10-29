package com.jcooky.karan.commons.nio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcooky.karan.commons.network.AbstractIOFactory;
import com.jcooky.karan.commons.network.AbstractSpec;
import com.jcooky.karan.commons.network.Acceptor;
import com.jcooky.karan.commons.network.Connector;
import com.jcooky.karan.commons.network.Spec;
import com.jcooky.karan.commons.network.Transfer;
import com.jcooky.karan.commons.network.Transport;
import com.jcooky.karan.commons.network.buffer.IoBuffer;
import com.jcooky.karan.commons.network.fields.Field;
import com.jcooky.karan.commons.network.fields.primitive.LongField;
import com.jcooky.karan.commons.network.fields.primitive.StringField;
import com.jcooky.karan.commons.network.nio.NIOFactory;
import com.jcooky.karan.commons.network.spec.SpecBuilder;
import com.jcooky.karan.commons.test.utils.R;

public class NIOTransferTest {
	private static final Logger logger = LoggerFactory.getLogger(NIOTransferTest.class);
	
	private AbstractIOFactory ioFactory = NIOFactory.get();
	private Connector connector = ioFactory.createConnector();
	private Acceptor acceptor = ioFactory.createAcceptor();
	private ExecutorService service = Executors.newCachedThreadPool();
	
	public static class TestSpec extends AbstractSpec {
		private Field<String> text = new StringField(null);
		private Field<Long> timestamp = new LongField(text);
		
		static {
			SpecBuilder.register((byte)0, TestSpec.class);
		}
		
		public TestSpec() {
			setHead(text);
		}

		public byte getType() {
			return 0;
		}
		
		public void setText(String text) {
			this.text.set(text);
		}
		
		public String getText() {
			return text.get();
		}
		
		public void setTimestamp(long timestamp) {
			this.timestamp.set(timestamp);
		}
		
		public Long getTimestamp() {
			return timestamp.get();
		}
	}
	
	@Before
	public void setUp() {
		acceptor.listen(8080);
	}
	
	@Test
	public void testSendAndRecv() throws Exception {
		final String text = IOUtils.toString(R.text, "UTF-8");
		final Long timestamp = System.nanoTime();
		service.execute(new Runnable() {
			public void run() {
				Transfer<Spec> transfer = null;
				try {
					Transport transport = null;
					while((transport = acceptor.accept()) == null);
					
					transfer = ioFactory.createTransfer(transport);
					TestSpec ts = new TestSpec();
					ts.setText(text);
					ts.setTimestamp(timestamp);
					
					transfer.send(ts);
				} finally {
					if (transfer != null) {
						transfer.close();
					}
				}
			}
		});

		TestSpec ts = null;
		Transport transport = connector.connect(InetAddress.getLocalHost(), 8080);
		Transfer<TestSpec> transfer = ioFactory.createTransfer(transport);
		while ((ts = transfer.recv()) == null);
		
		assertNotNull(ts);
		assertEquals(text, ts.getText());
		assertEquals(timestamp, ts.getTimestamp());
		
		transfer.close();
	}
}
