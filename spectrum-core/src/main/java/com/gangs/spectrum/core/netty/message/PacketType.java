package com.gangs.spectrum.core.netty.message;

import java.util.HashMap;
import java.util.Map;

public class PacketType {
	
	//----------------------模块号申明------------------------------
	//----------------请求协议id格式为 模块号_000 起--------------------
	//----------------推送协议id格式为 模块号_200 起--------------------
	//------------------基础服务1-----------------------------------
	
	private static Map<Integer,Class<? extends AbstractPacket>> PACKET_CLASS_MAP = new HashMap<>();

	public static void registerPackets(int type, Class<? extends AbstractPacket> packetClass) {
		PACKET_CLASS_MAP.put(type,packetClass);
	}	
	public static int getPacketType(Class<? extends AbstractPacket> packetClass) {
		for(int type : PACKET_CLASS_MAP.keySet()) {
			//only return the first one
			if(packetClass == PACKET_CLASS_MAP.get(type)) {
				return type;
			}
		}
		return 0;
	}
	
	public static Class<? extends AbstractPacket> getPacketClassBy(int packetType){
		return PACKET_CLASS_MAP.get(packetType);
	}
}
