package com.gangs.spectrum.core.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gangs.spectrum.core.netty.message.AbstractPacket;
import com.gangs.spectrum.core.netty.util.ChannelUtils;

import io.netty.channel.Channel;
import lombok.Data;

@Data
public class IoSession {
	private static final Logger logger = LoggerFactory.getLogger(IoSession.class);
	
	/** 网络连接channel */
	private Channel channel;
	
	/** ip地址 */
	private String ipAddr;
	
	public IoSession() {

	}

	public IoSession(Channel channel) {
		this.channel = channel;
		this.ipAddr = ChannelUtils.getIp(channel);
	}
	
	/**
	 * 关闭session
	 * @param reason {@link SessionCloseReason}
	 */
	public void close(SessionCloseReason reason) {
		try{
			if (this.channel == null) {
				return;
			}
			if (channel.isOpen()) {
				channel.close();
				logger.info("close session, reason is {}",  reason);
			}else{
				logger.info("session already close, reason is {}", reason);
			}
		}catch(Exception e){
		}
	}
	
	/**
	 * 发送消息
	 * @param packet
	 */
	public void sendPacket(AbstractPacket packet) {
		if (packet == null) {
			return;
		}
		if (channel != null) {
			channel.writeAndFlush(packet);
		}
	}
	
	public boolean isClose() {
		if (channel == null) {
			return true;
		}
		return !channel.isActive() ||
			   !channel.isOpen();
	}
}
