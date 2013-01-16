package com.github.karan.server.gateway;

import com.github.karan.server.gateway.gen.Gateway;
import com.github.karan.server.gateway.gen.InvalidExcuteException;
import com.github.karan.server.utils.ByteArrayClassLoader;
import com.github.karan.server.utils.ListableByteOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TIOStreamTransport;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GatewayImpl implements Gateway.Iface {

    private static final String TEMP_DIR = FileUtils.getTempDirectoryPath();
    private ByteArrayClassLoader classLoader = new ByteArrayClassLoader();
    private Map<String, TProcessor> processors = new HashMap<String, TProcessor>();

    @Override
    public boolean exists(String interfaceName) throws TException {
        return processors.containsKey(interfaceName);
    }

    @Override
    public void put(String interfaceName, String implClassName, List<String> classNames, List<ByteBuffer> classes) throws TException {
        Iterator<String> classNamesIterator = classNames.iterator();
        Iterator<ByteBuffer> classesIterator = classes.iterator();
        while (classesIterator.hasNext() && classNamesIterator.hasNext()) {
            ByteBuffer classBinary = classesIterator.next();
            String className = classNamesIterator.next();

            try {
                classLoader.loadClass(className, classBinary.array());
            } catch(ClassNotFoundException e) {
                throw new TException(e);
            }
        }

        if (classesIterator.hasNext() || classNamesIterator.hasNext())
            throw new TException("className & classes not pair");


        try {
            Class<?> ifaceCls = null;
            Class<? extends TProcessor> processorCls = null;
            for (Class<?> c : Class.forName(interfaceName, false, classLoader).getDeclaredClasses()) {
                if(c.getName().endsWith("Iface")) {
                    ifaceCls = c;
                } else if(c.getName().endsWith("Processor")) {
                    processorCls = (Class<? extends TProcessor>)c;
                }
            }

            if (ifaceCls != null && processorCls != null) {
                Constructor<?> constructor = processorCls.getConstructor(ifaceCls);
                TProcessor processor = processorCls.cast(constructor.newInstance(Class.forName(implClassName, true, classLoader)));
                processors.put(interfaceName, processor);
            }
        } catch (ClassNotFoundException e) {
            throw new TException(e);
        } catch (NoSuchMethodException e) {
            throw new TException(e);
        } catch (InstantiationException e) {
            throw new TException(e);
        } catch (IllegalAccessException e) {
            throw new TException(e);
        } catch (InvocationTargetException e) {
            throw new TException(e);
        }


    }

    @Override
    public void remove(String interfaceName) throws TException {
        processors.remove(interfaceName);
    }

    @Override
    public ByteBuffer execute(String interfaceName, final ByteBuffer thriftBinaries) throws InvalidExcuteException, TException {
        TProcessor processor = processors.get(interfaceName);
        if (processor == null)
            return null;

        ListableByteOutputStream os = new ListableByteOutputStream();

        processor.process(new TBinaryProtocol(new TIOStreamTransport(new InputStream() {
            @Override
            public int read() throws IOException {
                return (int)thriftBinaries.get();
            }
        })), new TBinaryProtocol(new TIOStreamTransport(os)));

        return ByteBuffer.wrap(os.toArray());
    }
}
