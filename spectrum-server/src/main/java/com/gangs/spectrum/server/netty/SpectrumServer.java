package com.gangs.spectrum.server.netty;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gangs.spectrum.core.netty.message.PacketType;
import com.gangs.spectrum.core.netty.message.codec.PacketDecoder;
import com.gangs.spectrum.core.netty.message.codec.PacketEncoder;
import com.gangs.spectrum.server.base.ServerNode;
import com.gangs.spectrum.server.config.NettyServerConfig;
import com.gangs.spectrum.server.netty.message.ServerPushCaculatedPoint;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.GenericFutureListener;

public class SpectrumServer implements ServerNode {	
	private Logger logger = LoggerFactory.getLogger(SpectrumServer.class);
	
	private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	
	private EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());	
	

    EventExecutorGroup businessGroup = new DefaultEventExecutorGroup(2);//业务线程池

	@Override
	public void init() {
		PacketType.registerPackets(ServerPushCaculatedPoint.packetType, ServerPushCaculatedPoint.class);
	}

	@Override
	public void start(NettyServerConfig nettyServerConfig) throws Exception {
		logger.info("server begin start....");
		try {
			//PacketType.registerPackets();
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChildChannelHandler());
			ChannelFuture f = b.bind(new InetSocketAddress(nettyServerConfig.getSocketPort())).sync();
			f.addListener(new GenericFutureListener<ChannelFuture>() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    logger.info("Netty init over,result:{}", future.isSuccess());
                }
			});
		}catch (Exception e){
            logger.error("server start error", e);
            throw e;
        }
        logger.info("server success start....");
	}

	@Override
	public void shutDown() throws Exception {
	}

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel arg0) throws Exception {
            ChannelPipeline pipeline = arg0.pipeline();
            pipeline.addLast(new PacketDecoder(1024 * 1024, 0, 4, 0, 4));
            pipeline.addLast(new LengthFieldPrepender(4));
            pipeline.addLast(new PacketEncoder());
            // 客户端300秒没收发包，便会触发UserEventTriggered事件到MessageTransportHandler
            pipeline.addLast("idleStateHandler", new IdleStateHandler(0, 0, 30000));
            pipeline.addLast(businessGroup, new IoHandler());
        }
    }
}
