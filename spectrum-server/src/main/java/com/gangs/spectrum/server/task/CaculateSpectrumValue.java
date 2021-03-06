package com.gangs.spectrum.server.task;

import com.gangs.spectrum.server.netty.message.ServerPushCaculatedPoint;

import io.netty.channel.Channel;

public class CaculateSpectrumValue implements Runnable {
	
	private long startTime;
	
	private Channel channel;
	
	private final static double a = 0;
	private final static double b = 2;
	private final static double c = 0;
	private final static double T = 5000;
	
	public CaculateSpectrumValue(Channel channel) {
		this.startTime = System.currentTimeMillis();
		this.channel = channel;
	}

	//	在加载段ab内，a点的值小于b点值：
	//A=(b-a)/2；ω=2π/T=2π/2T1=π/T1；θ=-π/2；C=(a+b)/2
	//则正弦曲线加载满足条件为F=(b-a)/2  sin⁡〖（π/T1 t-π/2）+(a+b)/2〗
	//	在加载段bc内，b点值大于c点值：
	//A=(b-c)/2；ω=2π/T=2π/2T1=π/T1；θ=π/2；C=(b+c)/2
	//则正弦曲线加载满足条件为F=(b-c)/2  sin⁡〖（π/T1 t+π/2）+(b+c)/2〗

	private static double computeSpectrumPoint(long t, double a, double b, double c, double T) {

		double A = 0d;
		double omega = 0d;
		double theta = 0d;
		double C = 0d;
		double F = 0d;

		if ((t % T) < (T / 2)) {
			A = (b - a) / 2d;
			omega = 2 * Math.PI / T;
			theta = -1.0d * Math.PI / 2d;
			C = (a + b) / 2d;
			F = A * Math.sin(omega * t + theta) + C;
		} else {
			A = (b - c) / 2d;
			omega = 2 * Math.PI / T;
			theta = Math.PI / 2d;
			C = (b + c) / 2d;
			F = A * Math.sin(omega * t + theta) + C;
		}

		return F;
	}

	@Override
	public void run() {
		long now = System.currentTimeMillis();
		double value = computeSpectrumPoint(now - startTime, a, b, c, T);
		ServerPushCaculatedPoint pcp = new ServerPushCaculatedPoint();
		pcp.setPointY(value);
		this.channel.writeAndFlush(pcp);
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

}
