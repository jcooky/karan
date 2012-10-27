package com.jcooky.karan.commons.nio;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcooky.karan.commons.network.AbstractIOFactory;
import com.jcooky.karan.commons.network.AbstractSpec;
import com.jcooky.karan.commons.network.Acceptor;
import com.jcooky.karan.commons.network.Connector;
import com.jcooky.karan.commons.network.buffer.IoBuffer;
import com.jcooky.karan.commons.network.fields.Field;
import com.jcooky.karan.commons.network.fields.primitive.LongField;
import com.jcooky.karan.commons.network.fields.primitive.StringField;
import com.jcooky.karan.commons.network.nio.NIOFactory;
import com.jcooky.karan.commons.network.spec.SpecBuilder;

public class NIOTransferTest {
	private static final Logger logger = LoggerFactory.getLogger(NIOTransferTest.class);
	
	private AbstractIOFactory ioFactory = NIOFactory.get();
	private Connector connector = ioFactory.createConnector();
	private Acceptor acceptor = ioFactory.createAcceptor();
	
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
		
	}
}
