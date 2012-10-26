package com.jcooky.karan.commons.network.nio;

import com.jcooky.karan.commons.network.AbstractIOFactory;
import com.jcooky.karan.commons.network.Session;
import com.jcooky.karan.commons.network.Spec;
import com.jcooky.karan.commons.network.Transfer;
import com.jcooky.karan.commons.network.buffer.IoBuffer;
import com.jcooky.karan.commons.network.fields.AbstractField;
import com.jcooky.karan.commons.network.fields.Field;
import com.jcooky.karan.commons.network.spec.SpecBuilder;

public class NIOSpecTransfer implements Transfer<Spec> {
	
	private Session session;
	private AbstractIOFactory ioFactory;
	
	public NIOSpecTransfer(AbstractIOFactory ioFactory, Session session) {
		this.session = session;
		this.ioFactory = ioFactory;
	}
	
	private void generateTypeBytes(IoBuffer buf, int type) {
		Field<Integer> codeField = AbstractField.getField("int"); 
		codeField.set(type); 
		buf.put(codeField.toBytes());
	}

	public void send(Spec spec) {
		IoBuffer buf = null;
		try {
			buf = ioFactory.createIoBuffer(); 
			generateTypeBytes(buf, spec.getType());
			for (Field<?> field = spec.getHeadField(); field != null; field = field.next()) {
				buf.put(field.toBytes());
			}
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
