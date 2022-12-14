package com.aicnfirst.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
	
	private String result;
	
	private List<String> loadAll;
	
	private List<String> nombres;

	public Response(String result) {
		super();
		this.result = result;
	}

	public Response(List<String> loadAll, List<String> nombres) {
		super();
		this.loadAll = loadAll;
		this.nombres = nombres;
	}

}
