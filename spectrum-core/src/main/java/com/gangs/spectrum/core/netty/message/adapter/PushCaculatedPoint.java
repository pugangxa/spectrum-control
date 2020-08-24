package com.gangs.spectrum.core.netty.message.adapter;

import com.gangs.spectrum.core.netty.IoSession;
import com.gangs.spectrum.core.netty.message.AbstractPacket;

import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class PushCaculatedPoint extends AbstractPacket {
	
	final static public int packetType = 1_200;
	private double pointY;
	
	@Override
	public void writeBody(ByteBuf buf) {
		buf.writeDouble(pointY);

	}

	@Override
	public void readBody(ByteBuf buf) {
		this.pointY = buf.readDouble();
	}
	
	@Override
	public void execPacket(IoSession session) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getPacketType() {
		return packetType;
	}
}
