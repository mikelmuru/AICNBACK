package com.aicnfirst.controller;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import com.aicnfirst.service.ValidationService;
// import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/aicn/pet")
@Api(value = "", tags= {"Pet Controller"})
@Tag(name = "Pet Controller", description = "Este controlador nos sirve para trabajar sobre la clase Pet. "
			+ "Podremos probar aqui la dependencia jackson-databind-nullable.")
public class PetController {
	
	// @Autowired
	// private ValidationService validationService;
	
	// private ObjectMapper jacksonMapper = new ObjectMapper();

}
