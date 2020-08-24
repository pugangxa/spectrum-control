package com.gangs.spectrum.client.netty.message;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.gangs.spectrum.client.service.ClientScheduledService;
import com.gangs.spectrum.core.netty.IoSession;
import com.gangs.spectrum.core.netty.message.adapter.PushCaculatedPoint;

public class ClientPushCaculatedPoint extends PushCaculatedPoint {
	
	@Override
	public void execPacket(IoSession session) {
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(System.currentTimeMillis())) + ": 收到服务端的计算值: " + getPointY());
		ClientScheduledService.setCurrentPointY(getPointY());
	}
}
