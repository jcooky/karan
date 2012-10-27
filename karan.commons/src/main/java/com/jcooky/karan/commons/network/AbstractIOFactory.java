package com.jcooky.karan.commons.network;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcooky.karan.commons.network.buffer.IoBuffer;

public abstract class AbstractIOFactory {
	private static final Logger logger = LoggerFactory.getLogger(AbstractIOFactory.class);
	
	private static Map<String, Class<? extends AbstractIOFactory>> registeredFactories = new HashMap<String, Class<? extends AbstractIOFactory>>();
	private static Map<String, AbstractIOFactory> instances = new HashMap<String, AbstractIOFactory>();
	
	public abstract Acceptor createAcceptor();
	public abstract Connector createConnector();
	public abstract Transport createTransport(SocketChannel socketChannel, BlockingQueue<IoBuffer> readQ);
	public abstract Session createSession(Transport transport);
	public abstract <T> Transfer<T> createTransfer(Session session);
	public abstract <T> Transfer<T> createTransfer(Transport transport);
	public abstract IoBuffer createIoBuffer(int capacity);
	public abstract IoBuffer createIoBuffer();
	
	public static void register(String type, Class<? extends AbstractIOFactory> factoryClass) {
		registeredFactories.put(type, factoryClass);
	}
	
	public static AbstractIOFactory get(String type) {
		try {
			if (!instances.containsKey(type)) {
				instances.put(type, registeredFactories.get(type).newInstance());
			}
			return instances.get(type);
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
}
