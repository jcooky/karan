package com.github.karan.core;

import java.util.HashMap;
import java.util.Map;

public class InstanceServiceRepository {
	private Map<String, Object> instanceMap = new HashMap<String, Object>();
	
	public Object put(String serviceName, Object instance) {
		return instanceMap.put(serviceName, instance);
	}
	
	public Object remove(String serviceName) {
		return instanceMap.remove(serviceName);
	}
	
	public Object get(String serviceName) {
		return instanceMap.get(serviceName);
	}
}
