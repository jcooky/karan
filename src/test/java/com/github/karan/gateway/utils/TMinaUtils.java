package com.github.karan.gateway.utils;

import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TTransportFactory;

import com.github.jcooky.mina.thrift.TIoAcceptorServerTransport;
import com.github.jcooky.mina.thrift.TIoSessionTransport;
import com.github.jcooky.mina.thrift.TMinaServer;

public class TMinaUtils {
	public TMinaServer getServer(int port, TProcessor processor) {
		TIoAcceptorServerTransport socket = new TIoAcceptorServerTransport(port);
		return new TMinaServer(new TMinaServer.Args(socket)
												.processor(processor)
												.protocolFactory(new TBinaryProtocol.Factory())
												.inputTransportFactory(new TIoSessionTransport.InputTransportFactory())
												.outputTransportFactory(new TTransportFactory()));
	}
}
