package com.github.karan.core.filter;

import net.sf.json.JSONObject;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

import com.github.karan.core.processor.Processor;
import com.github.karan.exception.MessageConvertException;
import com.github.karan.message.Constants;

public class MessageProcessFilter extends IoFilterAdapter {

	@Override
	public void exceptionCaught(NextFilter nextFilter, IoSession session,
			Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(nextFilter, session, cause);
	}

	@Override
	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) throws Exception {
		if (message instanceof JSONObject) {
			JSONObject msg = (JSONObject)message;
			Processor processor = null;
			switch(msg.getInt("messageType")) {
			default:
				throw new MessageConvertException("messageType`s value is not defined");
			}
		} else {
			throw new MessageConvertException("message must be Json object");
		}
	}

	@Override
	public void messageSent(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception {
		// TODO Auto-generated method stub
		super.messageSent(nextFilter, session, writeRequest);
	}
	
}
