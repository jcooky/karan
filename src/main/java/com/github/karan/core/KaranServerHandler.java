package com.github.karan.core;

import net.sf.json.JSONObject;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class KaranServerHandler extends IoHandlerAdapter {

	public void messageReceived(IoSession session, Object msg)
			throws Exception {
		if (msg instanceof JSONObject) {
			JSONObject message = (JSONObject)msg;
			switch(message.getInt("messageType")) {
			
			}
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(session, cause);
	}
	
}
