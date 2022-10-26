package com.aicnfirst.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EncodedImage {
	
	@NotNull(message = "This property cannot be null.")
	private String encodedFile;
	
	@NotNull(message = "Resultado obligatorio.")
	private String resultado;

}
