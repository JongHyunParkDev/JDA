package com.yj.yjbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class JdaBotApplication {
	public static void main(String[] args) {
		SpringApplication.run(JdaBotApplication.class, args);
	}

}
