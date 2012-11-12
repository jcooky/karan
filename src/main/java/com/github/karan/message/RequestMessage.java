package com.github.karan.message;

public class RequestMessage extends AbstractMessage {
	private String serviceName;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public static long size() {
		return getType().byteValue() + 
	}
}
