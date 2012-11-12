package com.github.karan.message;

public class MessageHeader {
	public static final int LENGTH = Byte.SIZE + Long.SIZE;
	
	private MessageType messageType;
	private long length;
	
	public MessageType getMessageType() {
		return messageType;
	}
	
	public void setMessageType(byte messageType) {
		setMessageType(MessageType.valueOf(messageType));
	}
	
	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	
	public long getLength() {
		return length;
	}
	
	public void setLength(long length) {
		this.length = length;
	}
	
	public static 
}
