package com.github.karan.client;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.github.karan.server.Server;
import com.github.karan.server.gateway.GatewayImpl;
import com.github.karan.server.gateway.gen.Gateway;

public class ClientTest {
	private static final int PORT = 9090;
	
	private @Spy Gateway.Iface mockGateway = new GatewayImpl();
	private Server server;
	private Client client;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		server = new Server();
		server.setGateway(mockGateway);
		server.setPort(PORT);
		server.serve();
		
		client = new Client();
		client.connect("localhost", PORT);
	}
	
	
	@After
	public void tearDown() throws Exception {
		if (client != null)
			client.close();
		
		if (server != null)
			server.stop();
	}
	
	@Test
	public void testPut() throws Exception {
		
	}
}
