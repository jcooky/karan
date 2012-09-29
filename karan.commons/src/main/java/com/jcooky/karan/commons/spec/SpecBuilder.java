package com.jcooky.karan.commons.spec;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcooky.karan.commons.Spec;
import com.jcooky.karan.commons.buffer.IoBuffer;
import com.jcooky.karan.commons.field.IntegerField;
import com.jcooky.karan.commons.util.FieldUtils;

public class SpecBuilder {
	public static class BuildException extends RuntimeException {
		public BuildException() {
			super();
		}
		
		public BuildException(Throwable e) {
			super(e);
		}
	}
	
	private static final Logger logger = LoggerFactory.getLogger(SpecBuilder.class);
	private final static Map<Integer, Class<? extends Spec>> specMap = new HashMap<Integer, Class<? extends Spec>>();
	
	public static void register(Integer type, Class<? extends Spec> cls) {
		specMap.put(type, cls);
	}
	
	public static Spec build(IoBuffer ioBuffer) {
		IntegerField type = new IntegerField(null);
		type.fromBytes(ioBuffer);
		
		Class<? extends Spec> specClass = specMap.get(FieldUtils.toInt(type));
		if (specClass != null) {
			Spec spec;
			try {
				spec = specClass.newInstance();
			} catch (InstantiationException e) {
				logger.error(e.getMessage(), e);
				throw new BuildException(e);
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
				throw new BuildException(e);
			}
			spec.build(ioBuffer);
			
			return spec;
		}
		
		return null;
	}
}
