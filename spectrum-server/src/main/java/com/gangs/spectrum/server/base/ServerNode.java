package com.gangs.spectrum.server.base;

import com.gangs.spectrum.server.config.NettyServerConfig;

public interface ServerNode {
    /**
     * 服务初始化
     */
    void init();

    /**
     *  服务启动
     * @throws Exception
     */
    void start(NettyServerConfig nettyServerConfig) throws Exception;

    /**
     * 服务关闭
     * @throws Exception
     */
    void shutDown() throws Exception;
}
