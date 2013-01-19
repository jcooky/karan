package com.github.karan.test;

import com.github.karan.test.gen.TestInfo;
import com.github.karan.test.gen.TestService;
import org.apache.thrift.TException;

/**
 * Created with IntelliJ IDEA.
 * User: jcooky
 * Date: 13. 1. 17.
 * Time: 오전 3:48
 * To change this template use File | Settings | File Templates.
 */
public class TestServiceImpl implements TestService.Iface {
    private TestInfo info;

    @Override
    public void add(TestInfo info) throws TException {
        this.info = info;
    }

    @Override
    public TestInfo get() throws TException {
        return info;
    }
}
