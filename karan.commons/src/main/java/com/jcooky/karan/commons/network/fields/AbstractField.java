package com.jcooky.karan.commons.network.fields;

import com.jcooky.karan.commons.network.fields.primitive.ByteField;
import com.jcooky.karan.commons.network.fields.primitive.IntegerField;
import com.jcooky.karan.commons.network.fields.primitive.StringField;

public abstract class AbstractField<T> implements Field<T> {
	protected T val;
	private Field<?> prev = null, next = null;
	
	public AbstractField(Field<?> prev) {
		this.prev = prev;
		if (this.prev != null) {
			((AbstractField<?>)(this.prev)).next = this;
		}
	}
	
	public void set(T val) {
		this.val = val;
	}
	
	public T get() {
		return val;
	}
	
	public <U> Field<U> next() {
		return (Field<U>)next;
	}
	
	public <U> Field<U> prev() {
		return (Field<U>)prev;
	}
	
	public static <U> Field<U> getField(String type) {
		return getField(type, null);
	}
	
	@SuppressWarnings("unchecked")
	public static <U> Field<U> getField(String type, Field<?> prev) {
		if ("string".equalsIgnoreCase(type)) {
			return (Field<U>)new StringField(prev);
		} else if ("int".equalsIgnoreCase(type)) {
			return (Field<U>)new IntegerField(prev);
		} else if ("byte".equalsIgnoreCase(type)) {
			return (Field<U>)new ByteField(prev);
		}
		
		return null;
	}
}
