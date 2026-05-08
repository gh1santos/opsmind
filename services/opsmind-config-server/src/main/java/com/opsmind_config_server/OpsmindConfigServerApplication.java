package com.opsmind_config_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class OpsmindConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpsmindConfigServerApplication.class, args);
	}

}
