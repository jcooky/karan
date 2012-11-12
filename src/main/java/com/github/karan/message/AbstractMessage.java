package com.github.karan.message;

public abstract class AbstractMessage {
	private MessageType type;

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}
}
