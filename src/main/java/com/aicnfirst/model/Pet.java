package com.aicnfirst.model;

import javax.validation.constraints.Size;

//ESTA CLASE TIENE COMO OBJETIVO PROBAR LA DEPENDENCIA DE JACKSON-DATABIND-NULLABLE

import org.openapitools.jackson.nullable.JsonNullable;

public class Pet {

	@Size(max = 10)
	public JsonNullable<String> name = JsonNullable.undefined();
	
	public Pet name(JsonNullable<String> name) {
		this.name = name;
		return this;
	}
}
