package com.jcooky.karan.commons.test.utils;

public class Counter {
	private int count = 0;
	
	public Counter() {
		this(0);
	}
	
	public Counter(int count) {
		this.count = count;
	}
	
	public synchronized void increment() {
		++count;
	}
	
	public synchronized void decrement() {
		--count;
	}
	
	public int count() {
		return count;
	}
}
