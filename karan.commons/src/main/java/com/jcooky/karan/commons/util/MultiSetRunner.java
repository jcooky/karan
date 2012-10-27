package com.jcooky.karan.commons.util;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiSetRunner implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(MultiSetRunner.class);
	
	private Set<Runnable> runnables = new HashSet<Runnable>();
	private int idleTime = 10;
	private boolean running = false;
	
	public MultiSetRunner() {
		
	}
	
	public MultiSetRunner(Runnable runnable) {
		add(runnable);
	}
	
	public void add(Runnable runnable) {
		runnables.add(runnable);
	}
	
	public void setIdleTime(int idleTime) {
		this.idleTime = idleTime;
	}
	
	public void stop() {
		logger.debug("MultiSetRunner Stop");
		running = false;
	}
	
	public void run() {
		running = true;
		logger.debug("MultiSetRunner Started");
		while(running) {
//			logger.debug("running");
			for (Runnable runnable : runnables) {
				runnable.run();
			}
			
//			try {
//				Thread.sleep(idleTime);
//			} catch(InterruptedException e) {
//				logger.error("Running 중 인터럽트를 걸지마시고, stop()를 이용해주세요.", e);
//				throw new RuntimeException(e);
//			}
		}
	}
}
