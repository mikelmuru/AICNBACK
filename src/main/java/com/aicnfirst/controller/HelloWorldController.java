package com.aicnfirst.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.aicnfirst.model.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
@RequestMapping("/aicn/helloworld")
@Api(value = "", tags= {"Hello World Controller"})
@Tag(name = "Hello World Controller", description = "Esto es un controlador de prueba para "
				+ "probar la conexion SpringBoot - Angular")
public class HelloWorldController {
	
	@GetMapping
	@ApiOperation(value = "Hello World endpoint",
				  notes = "Esto es un Hello World basico de prueba para comprobar la conexion"
							+ " entre Spring Boot  y Angular.")
	public ResponseEntity<Response> helloWorld() {
		return ResponseEntity.ok().body(new Response("Hello World desde el backend"));
	}

}
