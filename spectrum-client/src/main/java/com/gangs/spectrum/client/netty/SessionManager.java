package com.gangs.spectrum.client.netty;

import com.gangs.spectrum.core.netty.IoSession;
import com.gangs.spectrum.core.netty.message.AbstractPacket;

import io.netty.channel.Channel;

public enum SessionManager {
	INSTANCE;


	/** 通信会话 */
	private IoSession session;

	public void registerSession(Channel channel) {
		this.session = new IoSession(channel);
	}

	public void sendMessage(AbstractPacket request){
		this.session.sendPacket(request);
	}

	/**
	 * 是否已连上服务器
	 * @return
	 */
	public boolean isConnectedSever() {
		return this.session != null;
	}

}
