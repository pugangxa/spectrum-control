package com.gangs.spectrum.client.netty.message;

import com.gangs.spectrum.core.netty.IoSession;
import com.gangs.spectrum.core.netty.message.AbstractPacket;

import io.netty.buffer.ByteBuf;

public class ReqHeartBeat extends AbstractPacket {
	
	final static public int packetType = 1_000;

	@Override
	public void writeBody(ByteBuf buf) {
	}

	@Override
	public void readBody(ByteBuf buf) {
		
	}

	@Override
	public void execPacket(IoSession session) {
		System.out.println("收到客户端的心跳回复");
	}

	@Override
	public int getPacketType() {
		return packetType;
	}
}
