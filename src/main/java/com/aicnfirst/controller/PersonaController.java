package com.aicnfirst.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aicnfirst.model.Persona;
import com.aicnfirst.service.ValidationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

//@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
@RequestMapping("/aicn/persona")
@Api(value = "", tags= {"Persona Controller"})
@Tag(name = "Persona Controller", description = "Este controlador nos sirve para trabajar sobre la clase Persona."
		+ " Esta clase esta validada con javax-validation. Haremos varias pruebas para conocer como funciona.")
public class PersonaController {
	
	@Autowired
	private ValidationService validationService;
	
	private List<Persona> personas = new ArrayList<>();
	private List<String> violationsNewPersona = new ArrayList<>();
	
	@PostMapping("/nuevapersona")
	@ApiOperation(value = "Post de personas",
	  			  notes = "Aqui podemos registrar personas validadas por JAVAX-VALIDATION + HIBERNATE.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Persona registrada correctamente."),
			@ApiResponse(responseCode = "400", description = "Persona no validada.")
	})
	public ResponseEntity<?> registrarPersona(@RequestBody Persona miPersona) {
		violationsNewPersona.clear();
		Set<ConstraintViolation<Persona>> violations = validationService.validarPersona(miPersona);
		
		if (violations.isEmpty()) {
			personas.add(miPersona);
			return ResponseEntity.ok().body(miPersona);
		} else {
			for (ConstraintViolation<Persona> violation : violations) {
			    violationsNewPersona.add(violation.getMessage()); 
			}
			return ResponseEntity.badRequest().body(violationsNewPersona);
		}
	}
	
	@GetMapping("/listarpersonas")
	@ApiOperation(value = "Get de personas",
	  			  notes = "Aqui podemos listar todas las personas registradas.")
	public ResponseEntity<List<Persona>> getAll() {
		return ResponseEntity.ok(personas);
	}

}
