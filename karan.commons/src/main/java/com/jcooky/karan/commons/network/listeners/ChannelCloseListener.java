package com.jcooky.karan.commons.network.listeners;

import com.jcooky.karan.commons.network.Transport;

public interface ChannelCloseListener {
	public void onClosed(String channelId);
}
