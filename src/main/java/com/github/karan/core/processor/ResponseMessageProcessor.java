package com.github.karan.core.processor;

import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.github.karan.exception.MessageProcessException;
import com.github.karan.message.AbstractMessage;
import com.github.karan.message.ResponseMessage;

public class ResponseMessageProcessor implements MessageProcessor {
	public void process(IoSession session, AbstractMessage message, Map<String, Object> metadata) throws IllegalArgumentException {
		if (message instanceof ResponseMessage) {
			ResponseMessage msg = (ResponseMessage)message;
			if (!msg.isOk()) {
				throw new MessageProcessException("Response msg is not ok!");
			}
		} else {
			throw new IllegalArgumentException("message must be instance for ResponseMessage");
		}
	}
}
