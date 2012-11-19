package com.github.karan.message;

import net.sf.json.JSONObject;

public abstract class AbstractMessage {
	private int messageType;

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	
	private void toJSON(JSONObject obj) {
		obj.put("messageType", messageType);
		internalToJSON(obj);
	}
	
	protected abstract void internalToJSON(JSONObject obj) ;
	
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		this.toJSON(obj);
		
		return obj;
	}
}
