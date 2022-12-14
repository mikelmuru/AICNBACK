package com.aicnfirst.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class EncodedImage {
	
	// @NotNull(message = "This property cannot be null.")
	// @NonNull
	private String encodedFile;

	private String imagename;
	
	@NotNull(message = "Resultado obligatorio.")
	@NonNull
	private String resultado;

}
