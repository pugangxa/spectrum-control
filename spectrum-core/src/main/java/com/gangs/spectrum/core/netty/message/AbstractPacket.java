package com.gangs.spectrum.core.netty.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gangs.spectrum.core.netty.IoSession;

import io.netty.buffer.ByteBuf;

public abstract class AbstractPacket extends ByteBufBean {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	abstract public int getPacketType();

	@Override
	public void writeBody(ByteBuf buf) {
	}

	@Override
	public void readBody(ByteBuf buf) {
	}

	abstract public void execPacket(IoSession session);


	/**
	 *  是否开启gzip压缩(默认关闭)
	 *  消息体数据大的时候才开启，非常小的包压缩后体积反而变大，而且耗时
	 */
	public boolean isUseCompression() {
		return false;
	}


}
