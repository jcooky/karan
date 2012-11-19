package com.github.karan.core;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.github.karan.message.AbstractMessage;

public class RepositoryServerHandler extends IoHandlerAdapter {

	public void messageReceived(IoSession session, Object message)
			throws Exception {
		if (message instanceof AbstractMessage) {
			AbstractMessage msg = (AbstractMessage)message;
			
		}
	}
}
