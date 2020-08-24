package com.gangs.spectrum.server.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.gangs.spectrum.server.base.ServerNode;
import com.gangs.spectrum.server.config.NettyServerConfig;

@Component
public class NettyStartupRunner implements CommandLineRunner {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired  
    private NettyServerConfig nettyServerConfig;

	@Override
	public void run(String... args) throws Exception {
        ServerNode spectrumServer = new SpectrumServer();
        spectrumServer.init();
        spectrumServer.start(nettyServerConfig);
	}

}
