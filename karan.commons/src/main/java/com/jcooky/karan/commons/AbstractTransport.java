package com.jcooky.karan.commons;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.jcooky.karan.commons.listeners.TransportCloseListener;

public abstract class AbstractTransport implements Transport {
	private String id = UUID.randomUUID().toString();
	private Set<TransportCloseListener> cls = new HashSet<TransportCloseListener>();
	
	public String getId() {
		return id;
	}
	
	public void addCloseListener(TransportCloseListener cl) {
		cls.add(cl);
	}
	
	public void close() {
		for (TransportCloseListener cl : cls) {
			cl.onClosed(this);
		}
	}
}
