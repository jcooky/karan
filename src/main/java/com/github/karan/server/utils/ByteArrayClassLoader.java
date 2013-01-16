package com.github.karan.server.utils;

/**
 * Created with IntelliJ IDEA.
 * User: JCooky
 * Date: 13. 1. 16
 * Time: 오후 10:04
 * To change this template use File | Settings | File Templates.
 */
public class ByteArrayClassLoader extends ClassLoader {

    public void loadClass(String name, byte[] buffer, int offset, int length, boolean resolve) throws ClassNotFoundException {
        Class<?> c = super.defineClass(name, buffer, offset, length);
        if (resolve) {
            resolveClass(c);
        }
    }

    public void loadClass(String name, byte[] buffer, int offset, int length) throws ClassNotFoundException {
        loadClass(name, buffer, offset, length);
    }

    public void loadClass(String name, byte[] buffer) throws ClassNotFoundException {
        loadClass(name, buffer, 0, buffer.length);
    }
}
