package com.jcooky.karan.commons.network.listeners;

import com.jcooky.karan.commons.network.Transport;

public interface TransportCloseListener {
	public void onClosed(Transport transport);
}
