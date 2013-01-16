package com.github.karan.gateway;

import com.github.karan.gateway.gen.Gateway;
import com.github.karan.gateway.gen.InvalidExcuteException;
import org.apache.thrift.TException;

import java.nio.ByteBuffer;

public class GatewayImpl implements Gateway.Iface {

	@Override
	public String getService(String uri) throws TException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(String uri, ByteBuffer impls) throws TException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(String uri, ByteBuffer impls) throws TException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(String uri) throws TException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ByteBuffer execute(String uri, ByteBuffer thriftBinaries) throws InvalidExcuteException, TException {
		 return null;
	}

}
