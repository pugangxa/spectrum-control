package com.gangs.spectrum.core.netty.message.enums;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.gangs.spectrum.core.netty.IoSession;
import com.gangs.spectrum.core.netty.message.AbstractPacket;
import com.gangs.spectrum.core.netty.message.PacketType;

public enum PacketManager {
	INSTANCE;
	
	public  AbstractPacket createNewPacket(int packetType) throws IllegalArgumentException{
		Class<? extends AbstractPacket> packetClass = PacketType.getPacketClassBy(packetType);
		if(packetClass == null){
			throw new IllegalArgumentException("类型为"+packetType+"的包定义不存在");
		}
		AbstractPacket packet = null;
		try {
			packet = (AbstractPacket) packetClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException("类型为"+packetType+"的包实例化失败");
		}

		return packet;
	}
	
	public void execPacket(IoSession session, AbstractPacket pact){
		if(pact == null){ return;}

		try {
			Method m = pact.getClass().getMethod("execPacket", IoSession.class);
			m.invoke(pact, session);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
