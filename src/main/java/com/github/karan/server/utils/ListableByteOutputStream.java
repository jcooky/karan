package com.github.karan.server.utils;

import org.apache.commons.lang.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: JCooky
 * Date: 13. 1. 16
 * Time: 오후 11:00
 * To change this template use File | Settings | File Templates.
 */
public class ListableByteOutputStream extends OutputStream {
    private List<Byte> bytes = new ArrayList<Byte>();

    @Override
    public void write(int b) throws IOException {
        bytes.add((byte) b);
    }

    public byte[] toArray() {
        return ArrayUtils.toPrimitive(bytes.toArray(new Byte[]{}));
    }
}
