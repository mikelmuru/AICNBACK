package com.aicnfirst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
@EnableJpaAuditing
public class AicnFirstProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(AicnFirstProjectApplication.class, args);
	}
	
	@Bean
	public WebClient getWebClient() {
		Builder builder = WebClient.builder();
		return builder.build();
	}

}
