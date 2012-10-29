package com.jcooky.karan.commons.network.nio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcooky.karan.commons.network.AbstractIOFactory;
import com.jcooky.karan.commons.network.Session;
import com.jcooky.karan.commons.network.Spec;
import com.jcooky.karan.commons.network.Transfer;
import com.jcooky.karan.commons.network.buffer.IoBuffer;
import com.jcooky.karan.commons.network.fields.AbstractField;
import com.jcooky.karan.commons.network.fields.Field;
import com.jcooky.karan.commons.network.spec.SpecBuilder;

public class NIOSpecTransfer extends Transfer<Spec> {
	private static final Logger logger = LoggerFactory.getLogger(NIOSpecTransfer.class);
	private Session session;
	private AbstractIOFactory ioFactory;
	
	public NIOSpecTransfer(AbstractIOFactory ioFactory, Session session) {
		this.session = session;
		this.ioFactory = ioFactory;
	}

	public void send(Spec spec) {
		IoBuffer buf = null;
		try {
			buf = ioFactory.createIoBuffer();
			spec.toBytes(buf);
			buf.flip();
			logger.debug("sending bytes : [length={}, hex={}]", buf.remaining(), buf.getHexDump());
			session.send(buf);
		} finally {
			if (buf != null) {
				buf.free();
			}
		}
	}

	public Spec recv() {
		IoBuffer recvBuf = null;
		try {
			recvBuf = session.recv();
			if (recvBuf != null) {
				return SpecBuilder.build(recvBuf);
			}
		} finally {
			if (recvBuf != null) {
				recvBuf.free();			
			}
		}
		
		return null;
	}

	public void close() {
		session.close();
	}
	
}
