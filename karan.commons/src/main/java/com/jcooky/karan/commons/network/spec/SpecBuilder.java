package com.jcooky.karan.commons.network.spec;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcooky.karan.commons.network.Spec;
import com.jcooky.karan.commons.network.buffer.IoBuffer;
import com.jcooky.karan.commons.network.fields.AbstractField;
import com.jcooky.karan.commons.network.fields.Field;

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
	private final static Map<Byte, Class<? extends Spec>> specMap = new HashMap<Byte, Class<? extends Spec>>();
	
	public static void register(Byte type, Class<? extends Spec> cls) {
		specMap.put(type, cls);
	}
	
	public static Spec build(IoBuffer ioBuffer) {
		int type = ioBuffer.get();
		
		Class<? extends Spec> specClass = specMap.get(type);
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
