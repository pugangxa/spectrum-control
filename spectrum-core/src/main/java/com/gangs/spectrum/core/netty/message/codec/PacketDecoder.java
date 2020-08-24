package com.gangs.spectrum.core.netty.message.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.GZIPInputStream;

import com.gangs.spectrum.core.netty.message.AbstractPacket;
import com.gangs.spectrum.core.netty.message.enums.PacketManager;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.util.ReferenceCountUtil;

public class PacketDecoder extends LengthFieldBasedFrameDecoder {
	/**
	 *
	 * @param maxFrameLength      帧的最大长度
	 * @param lengthFieldOffset   length字段偏移的地址
	 * @param lengthFieldLength   length字段所占的字节长
	 * @param lengthAdjustment    修改帧数据长度字段中定义的值，可以为负数
	 *                            因为有时候我们习惯把头部记入长度,若为负数,则说明要推后多少个字段
	 * @param initialBytesToStrip 解析时候跳过多少个长度
	 */
	public PacketDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
			int initialBytesToStrip) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
	}

	@Override
	public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		ByteBuf frame = (ByteBuf) super.decode(ctx, in);
		if (frame == null) {
			return null;
		}
		if (frame.readableBytes() <= 0) {
			return null;
		}

		int packetType = frame.readInt();
		AbstractPacket packet = PacketManager.INSTANCE.createNewPacket(packetType);
		boolean useCompression = packet.isUseCompression();
		ByteBuf realBuf = decompression(frame, useCompression);
		packet.readBody(realBuf);
		ReferenceCountUtil.release(realBuf);
		return packet;
	}

	private ByteBuf decompression(ByteBuf sourceBuf, boolean useCompression) throws Exception {
		if (!useCompression) {
			return sourceBuf;
		}

		int bodyLength = sourceBuf.readInt(); // 先读压缩数据的长度
		byte[] sourceBytes = new byte[bodyLength];
		sourceBuf.readBytes(sourceBytes); // 得到压缩数据的字节数组

//解压缩
		ByteArrayInputStream bis = new ByteArrayInputStream(sourceBytes);
		GZIPInputStream gzip = new GZIPInputStream(bis);

		final int MAX_MSG_LENGTH = bodyLength * 2; // 假设压缩率最大为100%！！！！！
		byte[] content = new byte[MAX_MSG_LENGTH];
		int num = -1;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while ((num = gzip.read(content, 0, content.length)) != -1) {
			baos.write(content, 0, num);
		}
		baos.flush();
		gzip.close();
		bis.close();

//重新封装成ByteBuf对象
		ByteBuf resultBuf = Unpooled.buffer();
		byte[] realBytes = baos.toByteArray(); // 压缩前的实际数据
		resultBuf.writeBytes(realBytes);
		baos.close();

		return resultBuf;
	}

}
