package com.github.karan.server.gateway.utils;

import java.net.InetSocketAddress;

import com.github.jcooky.mina.thrift.TIoAcceptorServerTransport;
import com.github.jcooky.mina.thrift.TIoSessionTransport;
import com.github.jcooky.mina.thrift.TMinaServer;
import com.github.jcooky.mina.thrift.codec.TFrameProtocolCodecFactory;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TTransportFactory;

public class TMinaUtils {
	public TMinaServer getServer(int port, TProcessor processor) {
		NioSocketAcceptor acceptor;
		acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);
		acceptor.setReuseAddress(true);

		acceptor.setDefaultLocalAddress(new InetSocketAddress(port));

		acceptor.getFilterChain().addLast(TIoAcceptorServerTransport.CODEC_NAME,
				new ProtocolCodecFilter(new TFrameProtocolCodecFactory()));

		TIoAcceptorServerTransport socket = new TIoAcceptorServerTransport(acceptor);
		return new TMinaServer(new TMinaServer.Args(socket).processor(processor)
				.protocolFactory(new TBinaryProtocol.Factory())
				.inputTransportFactory(new TTransportFactory())
				.outputTransportFactory(new TTransportFactory()));
	}
}
