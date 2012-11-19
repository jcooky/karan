package com.github.karan.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.github.karan.exception.ServicePutException;

public class ServiceRepository {
	private Map<String, Object> services = Collections.synchronizedMap(new HashMap<String, Object>());
	
	public void put(String serviceName, Class<?> serviceClazz) {
		try {
			Object impl = serviceClazz.newInstance();
			services.put(serviceName, impl);
		} catch (InstantiationException e) {
			throw new ServicePutException(e);
		} catch (IllegalAccessException e) {
			throw new ServicePutException(e);
		}
	}
	
	public void remove(String serviceName) {
		services.remove(serviceName);
	}
	
	public <T> T get(String serviceName, Class<T> interfaceClazz) {
		return interfaceClazz.cast(services.get(serviceName));
	}
}
