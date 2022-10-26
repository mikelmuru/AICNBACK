package com.aicnfirst.model;

import java.util.List;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//ESTA CLASE ESTA VALIDADA CON JAVAX-VALIDATION

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Persona {
	
//	@NotNull(message = "El nombre es obligatorio.") ---- ACEPTA UN PARAMETRO VACIO / ERROR SI NO EXISTE EL PARAMETRO
	@NotBlank(message = "El nombre es obligatorio.")
	private String nombre;
	
	@NotEmpty(message = "Las personas tienen apellidos.")
	private String apellidos;
	
	@Positive(message = "Nadie tiene menos de 0 años.")
	@Min(value = 18, message = "No puedes registrar menores de edad.")
	@Max(value = 120, message = "Imposible seguir vivo.")
	private Integer edad;
	
	@AssertTrue(message = "No puedes registrar un muerto.")
	private boolean vivo;
	
	@Email(message = "Debes añadir un mail valido.")
	private String email;
	
	private List<@NotBlank(message = "Todos tenemos aficiones.") String> aficiones;

}
