package com.aicnfirst.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class EncodedImage extends MyAuditable{

	private Integer id;
	private String encodedFile;
	private String imagename;
	@NotNull(message = "Resultado obligatorio.")
	@NonNull
	private String resultado;
	
	private String description;
}
