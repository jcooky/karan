package com.github.karan.core.processor;

import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.github.karan.core.InstanceServiceRepository;
import com.github.karan.core.Repository;
import com.github.karan.exception.MessageProcessException;
import com.github.karan.message.AbstractMessage;
import com.github.karan.message.RequestMessage;
import com.github.karan.message.ResponseMessage;

public class RequestMessageProcessor implements MessageProcessor {

	public void process(IoSession session, AbstractMessage message, Map<String, Object> metadata) {
		Repository repository = (Repository)metadata.get("repository");
		InstanceServiceRepository isr = (InstanceServiceRepository)metadata.get("instanceServiceRepository");
		if (message instanceof RequestMessage) {
			ResponseMessage reponseMessage = new ResponseMessage();
			RequestMessage msg = (RequestMessage)message;
			Object instance = null;
			try {
				instance = repository.get(msg.getServiceName(), Class.forName(msg.getInterfaceName()));
			} catch (ClassNotFoundException e) {
				reponseMessage.setOk(false);
				session.write(reponseMessage);
				throw new MessageProcessException(e);
			}
			
			// response
			reponseMessage.setOk(true);
			session.write(reponseMessage);
		} else {
			throw new IllegalArgumentException("message have to instance for RequestMessage");
		}
	}
	
}
