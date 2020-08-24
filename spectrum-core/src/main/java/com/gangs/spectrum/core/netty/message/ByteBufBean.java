package com.gangs.spectrum.core.netty.message;


import io.netty.buffer.ByteBuf;

import java.io.UnsupportedEncodingException;

public abstract class ByteBufBean {

	abstract public void writeBody(ByteBuf buf);

	abstract public void readBody(ByteBuf buf);

	protected  String readUTF8(ByteBuf buf){
		int strSize = buf.readInt();
		byte[] content = new byte[strSize];
		buf.readBytes(content);
		try {
			return new String(content,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}

	}

	protected  void writeUTF8(ByteBuf buf, String msg){
		byte[] content ;
		try {
			content = msg.getBytes("UTF-8");
			buf.writeInt(content.length);
			buf.writeBytes(content);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
