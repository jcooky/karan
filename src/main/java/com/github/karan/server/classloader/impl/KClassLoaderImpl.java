package com.github.karan.server.classloader.impl;

import com.github.karan.KException;
import com.github.karan.server.classloader.KClassLoader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created with IntelliJ IDEA.
 * User: JCooky
 * Date: 13. 1. 16
 * Time: 오후 10:04
 * To change this template use File | Settings | File Templates.
 */
public class KClassLoaderImpl extends KClassLoader {
    private final static File TEMP_DIR = SystemUtils.getJavaIoTmpDir();
    private URLClassLoader classLoader;

    public KClassLoaderImpl() {
        try {
            classLoader = new URLClassLoader(new URL[]{TEMP_DIR.toURL()});
        } catch (MalformedURLException e) {
            throw new KException(e);
        }
    }


    public Class<?> loadClass(String name, byte[] buffer, int offset, int length) throws KException, ClassNotFoundException {
        String filename = StringUtils.join(name.split("\\."), "/");
        File classFile = new File(TEMP_DIR, filename + ".class");
        byte []dest = new byte[length];
        System.arraycopy(buffer, offset, dest, 0, length);

        try {
            FileUtils.writeByteArrayToFile(classFile, dest);
        } catch(IOException e) {
            throw new KException(e);
        }

        return classLoader.loadClass(name);
    }
}
