package com.github.karan.message;


public class ResponseMessage extends AbstractMessage {
	private boolean ok;

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

}
