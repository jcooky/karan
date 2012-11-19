package com.github.karan.core.processor;

import org.apache.mina.core.session.IoSession;

import com.github.karan.core.ServiceRepository;
import com.github.karan.message.AbstractMessage;

public interface Processor {
	public IoSession getSession();
	
	public void process();
}
