package com.gangs.spectrum.server.netty;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gangs.spectrum.core.netty.IoSession;
import com.gangs.spectrum.core.netty.message.AbstractPacket;
import com.gangs.spectrum.core.netty.message.enums.PacketManager;
import com.gangs.spectrum.core.netty.util.ChannelUtils;
import com.gangs.spectrum.server.task.CaculateSpectrumValue;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.ScheduledFuture;

public class IoHandler extends ChannelInboundHandlerAdapter {
	private final static Logger logger = LoggerFactory.getLogger(IoHandler.class);
	
	private ScheduledFuture<?> caculateSpectrumValueScheduled;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		if(!ChannelUtils.addChannelSession(ctx.channel(), new IoSession(ctx.channel()))) {
			ctx.channel().close();
			logger.info("Duplicate session,IP=[{}]",ChannelUtils.getIp(ctx.channel()));
		}
		//启动定时器
		caculateSpectrumValueScheduled = ctx.executor().scheduleAtFixedRate(new CaculateSpectrumValue(ctx.channel()), 1000, 1, TimeUnit.MILLISECONDS);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		AbstractPacket message = (AbstractPacket) msg;
		logger.info("receive pact, content is {}", message.getClass().getSimpleName());

		final Channel channel = ctx.channel();
		IoSession session = ChannelUtils.getSessionBy(channel);

		// 当前先在io线程处理
		PacketManager.INSTANCE.execPacket(session, message);
	
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("业务流程出错", cause);
		//cause.printStackTrace();
		Channel channel = ctx.channel();
		if(cause instanceof IOException && channel.isActive()) {
			logger.error("simpleclient"+channel.remoteAddress()+"异常");
			//中止定时器, 关channel
			caculateSpectrumValueScheduled.cancel(true);
			ctx.close();
		}
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		//心跳包检测读超时
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			if (e.state() == IdleState.ALL_IDLE) {
				logger.info("客户端读超时");
				// 中止定时器，关channel
				caculateSpectrumValueScheduled.cancel(true);
				ctx.close();
			}
		}
	}
}
