package com.github.karan.core.processor;

import java.util.Map;

import org.apache.mina.core.session.IoSession;

import com.github.karan.message.AbstractMessage;

public interface MessageProcessor {
	public void process(IoSession session, AbstractMessage message, Map<String, Object> metadata) throws IllegalArgumentException;
}
