package com.gangs.spectrum.client.netty;

import java.net.InetSocketAddress;

import com.gangs.spectrum.client.util.PropertiesUtil;
import com.gangs.spectrum.core.netty.message.codec.PacketDecoder;
import com.gangs.spectrum.core.netty.message.codec.PacketEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;

public class SocketClient {
	/** 当前重接次数*/
	private int reconnectTimes = 0;
	
	public void start() {
		try{
			connect(PropertiesUtil.getStr("REMOTE_SERVER_IP"),
					PropertiesUtil.getInt("REMOTE_SERVER_PORT"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void connect(String host, int port) throws Exception{
		EventLoopGroup group = new NioEventLoopGroup(1);
		try{
			Bootstrap b  = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>(){

				@Override
				protected void initChannel(SocketChannel arg0)
						throws Exception {
					ChannelPipeline pipeline = arg0.pipeline();
					pipeline.addLast(new PacketDecoder(1024*1024,0,4,0,4));
					pipeline.addLast(new LengthFieldPrepender(4));//https://blog.csdn.net/u010853261/article/details/55803933
					pipeline.addLast(new PacketEncoder());
					pipeline.addLast(new ClientTransportHandler());
				}
			});
			
			ChannelFuture f = b.connect(new InetSocketAddress(host, port))
					.sync();
			f.channel().closeFuture().sync();
			resetReconnectTimes();
			System.out.println("client start success....");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//          group.shutdownGracefully();  //这里不再是优雅关闭了
			//设置最大重连次数，防止服务端正常关闭导致的空循环
			if (reconnectTimes < PropertiesUtil.getInt("MAX_RECONNECT_TIMES")) {
				reConnectServer();
			}
		}
	}
	
	/**
	 * 断线重连
	 */
	private void reConnectServer(){
		try {
			Thread.sleep(5000);
			System.err.println("客户端进行断线重连");
			connect(PropertiesUtil.getStr("REMOTE_SERVER_IP"),
					PropertiesUtil.getInt("REMOTE_SERVER_PORT"));
			reconnectTimes++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 重置重连次数
	 */
	public void resetReconnectTimes() {
		if (reconnectTimes > 0) {
			reconnectTimes = 0;
			System.err.println("断线重连成功");
		}
	}
}
