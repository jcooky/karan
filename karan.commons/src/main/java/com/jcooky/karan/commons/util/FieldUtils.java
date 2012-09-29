package com.jcooky.karan.commons.util;

import com.jcooky.karan.commons.Field;

public class FieldUtils {
	public static Integer toInt(Field field) {
		return toObject(field, Integer.class);
	}
	
	public static String toString(Field field) {
		return toObject(field, String.class);
	}
	
	public static Byte toByte(Field field) {
		return toObject(field, Byte.class);
	}
	
	public static Double toDouble(Field field) {
		return toObject(field, Double.class);
	}
	
	public static <T> T toObject(Field field, Class<T> cls) {
		return cls.equals(field.getPrimitiveClass()) ? (T)field.getData() : null;
	}
}
