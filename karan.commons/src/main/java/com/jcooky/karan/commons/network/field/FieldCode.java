package com.jcooky.karan.commons.network.field;

public enum FieldCode {
	BYTE((byte)0x00),
	INTEGER((byte)0x01),
	STRING((byte)0x02);
	
	private byte code;
	
	private FieldCode(byte code) {
		this.code = code;
	}
	
	public byte code() {
		return code;
	}
	
	public boolean equals(byte code) {
		return this.code == code;
	}
}
