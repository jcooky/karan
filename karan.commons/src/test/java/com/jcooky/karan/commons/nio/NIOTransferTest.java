package com.jcooky.karan.commons.nio;

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
	
	private static class TestSpec extends AbstractSpec {
		private Field<String> text = new StringField(null);
		private Field<Long> timestamp = new LongField(text);
		
		static {
			SpecBuilder.register(0, TestSpec.class);
		}
		
		public TestSpec() {
			setHead(text);
		}

		public int getType() {
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
	public void testSendAndRecv() {
		service.execute(new Runnable() {
			public void run() {
				Transfer<Spec> transfer = null;
				try {
					Transport transport = null;
					while((transport = acceptor.accept()) == null);
					
					transfer = ioFactory.createTransfer(transport);
					TestSpec ts = new TestSpec();
					ts.setText(IOUtils.toString(R.text, "UTF-8"));
					ts.setTimestamp(System.nanoTime());
					
					transfer.send(ts);
				} catch(Exception e) {
					throw new RuntimeException();
				} finally {
					if (transfer != null) {
						transfer.close();
					}
				}
			}
		});
		
		
	}
}
