package com.github.karan.core.filter;

import net.sf.json.JSONObject;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.DefaultWriteRequest;
import org.apache.mina.core.write.WriteRequest;

import com.github.karan.message.AbstractMessage;
import com.github.karan.message.Constants;
import com.github.karan.message.RequestMessage;

public class MyMessageFilter extends IoFilterAdapter {

	public void messageReceived(NextFilter nextFilter, IoSession session,
			Object message) throws Exception {
		if (message instanceof JSONObject) {
			JSONObject msg = (JSONObject)message;
			AbstractMessage outMsg = null;
			switch(msg.getInt("messageType")) {
			case Constants.MESSAGE_TYPE_REQUEST:
				outMsg = (AbstractMessage)JSONObject.toBean(msg, RequestMessage.class);
				break;
			case Constants.MESSAGE_TYPE_REPONSE:
				break;
			case Constants.MESSAGE_TYPE_CALL:
				break;
			case Constants.MESSAGE_TYPE_RETURN:
				break;
			case Constants.MESSAGE_TYPE_EXCEPTION:
				break;
			}
			super.messageReceived(nextFilter, session, outMsg);
		} else {
			super.messageReceived(nextFilter, session, message);
		}
	}

	public void messageSent(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception {
		super.messageSent(nextFilter, session, _write(writeRequest));
	}

	public void filterWrite(NextFilter nextFilter, IoSession session,
			WriteRequest writeRequest) throws Exception {
		super.filterWrite(nextFilter, session, _write(writeRequest));
	}
	
	private WriteRequest _write(WriteRequest writeRequest) {
		if (writeRequest.getMessage() instanceof AbstractMessage) {
			return new DefaultWriteRequest(JSONObject.fromObject(writeRequest.getMessage()), writeRequest.getFuture(), writeRequest.getDestination());
		} else {
			return writeRequest;
		}
	}
}
