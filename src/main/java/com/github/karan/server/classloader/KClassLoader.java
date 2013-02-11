package com.github.karan.server.classloader;

import com.github.karan.KException;

/**
 * Created with IntelliJ IDEA.
 * User: JCooky
 * Date: 13. 1. 16
 * Time: 오후 10:04
 * To change this template use File | Settings | File Templates.
 */
public abstract class KClassLoader {

    public abstract Class<?> loadClass(String name, byte[] buffer, int offset, int length) throws KException, ClassNotFoundException;
    public Class<?> loadClass(String name, byte[] buffer) throws ClassNotFoundException {
        return loadClass(name, buffer, 0, buffer.length);
    }
}
