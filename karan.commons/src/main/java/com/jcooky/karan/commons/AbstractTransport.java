package com.jcooky.karan.commons;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.jcooky.karan.commons.listeners.CloseListener;

public abstract class AbstractTransport implements Transport {
	private String id = UUID.randomUUID().toString();
	private Set<CloseListener> cls = new HashSet<CloseListener>();
	
	public String getId() {
		return id;
	}
	
	public void addCloseListener(CloseListener cl) {
		cls.add(cl);
	}
	
	public void close() {
		for (CloseListener cl : cls) {
			cl.onClosed();
		}
	}
}
