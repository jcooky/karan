package com.jcooky.karan.commons.nio;

import com.jcooky.karan.commons.AbstractIOFactory;
import com.jcooky.karan.commons.Field;
import com.jcooky.karan.commons.Session;
import com.jcooky.karan.commons.Spec;
import com.jcooky.karan.commons.Transfer;
import com.jcooky.karan.commons.buffer.IoBuffer;
import com.jcooky.karan.commons.field.IntegerField;
import com.jcooky.karan.commons.spec.SpecBuilder;

public class NIOSpecTransfer extends Transfer<Spec> {
	
	private Session session;
	private AbstractIOFactory ioFactory;
	
	public NIOSpecTransfer(AbstractIOFactory ioFactory, Session session) {
		this.session = session;
		this.ioFactory = ioFactory;
	}
	
	private void generateTypeBytes(IoBuffer buf, int type) {
		Field codeField = new IntegerField(null); 
		codeField.setData(type); 
		buf.put(codeField.toBytes());
	}

	public void send(Spec spec) {
		IoBuffer buf = ioFactory.createIoBuffer(); 
		generateTypeBytes(buf, spec.getType());
		for (Field field = spec.getHeadField(); field != null; field = field.getNext()) {
			buf.put(field.toBytes());
		}
		session.send(buf);
		buf.free();
	}

	public Spec recv() {
		IoBuffer recvBuf = session.recv();
		if (recvBuf != null) {
			return SpecBuilder.build(recvBuf);
		}
		recvBuf.free();
		
		return null;
	}

	public void close() {
		session.close();
	}
	
}
