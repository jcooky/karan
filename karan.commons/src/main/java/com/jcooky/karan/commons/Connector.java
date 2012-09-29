package com.jcooky.karan.commons;

import java.net.InetAddress;

public interface Connector {
	public Transport connect(InetAddress host, int port);
}
