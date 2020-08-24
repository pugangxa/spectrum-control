package com.gangs.spectrum.core.netty;

public enum SessionCloseReason {
	/** 正常退出 */
	NORMAL,
	
	/** 链接超时 */
	OVER_TIME,
}
