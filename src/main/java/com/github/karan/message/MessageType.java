package com.github.karan.message;

public enum MessageType {
	REQUEST((byte)1), RESPONSE((byte)2), CALL((byte)3), RETURN((byte)4);
	
	private byte type;
	
	private MessageType(byte type) {
		this.type = type;
	}
	
	public byte byteValue() {
		return type;
	}
	
	public static MessageType valueOf(byte type) {
		for (MessageType t : MessageType.values()) {
			if (t.byteValue() == type) {
				return t;
			}
		}
		
		return null;
	}
}
