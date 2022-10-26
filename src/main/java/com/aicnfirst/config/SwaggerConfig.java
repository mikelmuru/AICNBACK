package com.aicnfirst.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
	
	@Bean
	public Docket filesBeans() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.regex("/aicn.*"))
				.build()
				.pathMapping("/")
				.apiInfo(getInfo());
	}
	
	public ApiInfo getInfo() {
		return new ApiInfoBuilder().title("AICN First Project")
				.description("Este proyecto es una prueba. "
						+ "Subida y peticion de imagenes con SpringBoot y Angular.")
				.license("Licencia de Mikel")
				.build();
	}
}
