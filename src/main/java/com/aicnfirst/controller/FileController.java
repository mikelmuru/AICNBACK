package com.aicnfirst.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aicnfirst.model.EncodedImage;
import com.aicnfirst.model.InputReq;
import com.aicnfirst.service.FileService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
@RestController
@RequestMapping("/aicn/files")
@Api(value = "", tags= {"File Controller"})
@Tag(name = "File Controller", description = "Este es el controlador de las imagenes. "
		+ "Tiene 3 endpoint: HelloWorld, UploadImage, LoadImage.")
public class FileController {
	
	@Autowired
	private FileService service;
	
	private Logger log = LoggerFactory.getLogger(FileController.class);
	
//	============================UPLOAD FILE METHOD==============================
	@PostMapping("/upload")
	@ApiOperation(value = "Post de imagenes",
	  			  notes = "Aqui cargamos desde angular las imagenes que uqeremos guardar en la carpeta.")
	public ResponseEntity<List<EncodedImage>> uploadFiles(@RequestParam("files") List<MultipartFile> files) throws Exception {
		log.info("He entrado a uploadFiles.");
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(service.saveAll(files));
	}

	@PostMapping("/tryapp")
	@ApiOperation(value = "Post de imagenes en modo PRUEBA",
	  			  notes = "Aqui cargamos desde angular las imagenes que uqeremos guardar en la carpeta.")
	public ResponseEntity<List<EncodedImage>> uploadFilesTryMode(@RequestParam("files") List<MultipartFile> files) throws Exception {
		log.info("He entrado a uploadFiles.");
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(service.tryApp(files));
	}

//	============================UPDATE FILE METHOD==============================
	@PutMapping("/update")
	public ResponseEntity<List<EncodedImage>> updateImage(@RequestParam("file") InputReq data) {

		return ResponseEntity.status(HttpStatus.OK).body(service.updateAuditable(data));
	}

//	============================PRUEBAS AUDITABLE=============================

	// @PostMapping("/auditableSave")
	// public ResponseEntity<String> auditableSave(@RequestBody InputReq request) {
	// 	log.info(request.toString());
	// 	return ResponseEntity.ok().body(service.saveAuditable(request));
	// }

	// @PutMapping("/auditableUpdate")
	// public ResponseEntity<String> auditableUpdate(@RequestBody InputReq request) {
	// 	log.info(request.toString());
	// 	return ResponseEntity.ok().body(service.updateAuditable(request));
	// }
	
//	============================GET FILES METHOD==============================
//	CON {---:.+} INDICAMOS QUE filename TIENE UNA EXTENSION (DIFERNETE SEGUN EL TIPO DE IMG)
	@GetMapping(path = {"/searchImages","/searchImages/{filter}"})
	@ApiOperation(value = "Buscador de imagenes",
				  notes = "Aqui podemos indicar el nombre y extension de una imagen para buscarla"
				  			+ " en la carpeta de imagenes.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Imagen encontrada."),
			@ApiResponse(responseCode = "404", description = "Imagen no encontrada en la carpeta.")
	})
	public ResponseEntity<List<EncodedImage>> getFileByName(@PathVariable(required = false) Optional<String> filter) throws Exception {
		log.info("He entrado a getFileByName.");

		List<EncodedImage> busqueda = new ArrayList<>();

		if (filter.isPresent()) {
			busqueda = service.filterControl(filter.get());
		} else {
			busqueda = service.searchAll();
		}

		if (busqueda.size() > 0) {
			return ResponseEntity.ok().body(busqueda);
		} else {
			return ResponseEntity.badRequest().body(busqueda);
		}
	}
	
//	============================LOAD ALL FILE METHOD==============================
//	AQUI CARGAMOS LAS IMAGENES DE PRUEBA PARA EL APARTADO DE PROBAR LA APP
	@GetMapping("/loadAll")
	@ApiOperation(value = "Cargamos todas las imagenes.",
				  notes = "Aqui devolvemos todas las imagenes en base64.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Imagenes convertidas y enviadas."),
			@ApiResponse(responseCode = "404", description = "Imagenes no convertidas.")
	})
	public ResponseEntity<List<EncodedImage>> loadAll() throws Exception{
		log.info("He entrado a loadAll.");
		
		return ResponseEntity.ok().body(service.loadAll());
	}

//	============================DELETE FILE METHOD==============================
//	AQUI ELIMINAMOS LAS IMAGENES (POR ID - TODAS)
@DeleteMapping("/delete/{id}")
@ApiOperation(value = "Eliminamos imagen por id.")
@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Imagen eliminada con exito.")
})
public ResponseEntity<?> deleteImage(@PathVariable Integer id) throws Exception{
	log.info("He entrado a loadAll.");
	
	return ResponseEntity.ok().body(service.deleteById(id));
}
}
