package com.github.karan;

import com.github.karan.server.Server;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: jcooky
 * Date: 13. 1. 16.
 * Time: 오전 11:56
 * To change this template use File | Settings | File Templates.
 */
public class ServerTest {
    private Server server;

    @Before
    public void setUp() throws Exception {
        server = new Server();
        server.serve(8080);
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testServe() {
        Assert.assertTrue(true);
    }
}
