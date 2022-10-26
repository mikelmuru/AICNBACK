package com.aicnfirst.service;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.stereotype.Service;

import com.aicnfirst.model.Persona;
import com.aicnfirst.model.Pet;

@Service
public class ValidationService {
	
//	ESTAS VARIABLES NOS DAN LAS HERRAMIENTAS PARA VALIDAR LAS INSTANCIAS DE NUESTRAS CLASES
//	factory NOS DA LA OPCION DE CREAR UN VALIDADOR (validator)
	
	private ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private Validator validator = factory.getValidator();
	
	public Set<ConstraintViolation<Persona>> validarPersona (Persona persona) {
		return validator.validate(persona);
	}
	
	public Set<ConstraintViolation<Pet>> validarPet (Pet pet) {
		return validator.validate(pet);
	}

}
