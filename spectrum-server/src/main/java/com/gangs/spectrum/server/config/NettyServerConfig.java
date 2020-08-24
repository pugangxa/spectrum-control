package com.gangs.spectrum.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@ConfigurationProperties(prefix = "server")
@Data
@Component
public class NettyServerConfig {
	private String socketIp;
	
	private int socketPort;
	
	private int httpPort;
}
