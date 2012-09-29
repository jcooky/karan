package com.jcooky.karan.commons;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcooky.karan.commons.buffer.IoBuffer;

public abstract class AbstractIOFactory {
	private static final Logger logger = LoggerFactory.getLogger(AbstractIOFactory.class);
	private static Map<String, Class<? extends AbstractIOFactory>> registeredFactories = new HashMap<String, Class<? extends AbstractIOFactory>>();
	
	public abstract Transport createTransport(SocketChannel socketChannel, BlockingQueue<IoBuffer> readQ);
	public abstract IoBuffer createIoBuffer(int capacity);
	public abstract IoBuffer createIoBuffer();
	
	public static void register(String type, Class<? extends AbstractIOFactory> factoryClass) {
		registeredFactories.put(type, factoryClass);
	}
	
	public static AbstractIOFactory create(String type) {
		try {
			return registeredFactories.get(type).newInstance();
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
}
