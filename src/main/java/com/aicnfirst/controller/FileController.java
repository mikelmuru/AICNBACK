package com.aicnfirst.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aicnfirst.model.EncodedImage;
import com.aicnfirst.model.Response;
import com.aicnfirst.service.FileService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
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
	public ResponseEntity<ArrayList<EncodedImage>> uploadFiles(@RequestParam("files") List<MultipartFile> files) throws Exception {
		log.info("He entrado a uploadFiles.");
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(service.saveAll(files));
	}
	
//	============================GET FILE METHOD==============================
//	CON {---:.+} INDICAMOS QUE filename TIENE UNA EXTENSION (DIFERNETE SEGUN EL TIPO DE IMG)
	@GetMapping("/{filename}")
	@ApiOperation(value = "Buscador de imagenes",
				  notes = "Aqui podemos indicar el nombre y extension de una imagen para buscarla"
				  			+ " en la carpeta de imagenes.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Imagen encontrada."),
			@ApiResponse(responseCode = "404", description = "Imagen no encontrada en la carpeta.")
	})
	public ResponseEntity<EncodedImage> getFile(@PathVariable int filename) throws Exception {
		log.info("He entrado a getFile.");

		EncodedImage busqueda = service.load2(filename);
		log.info(busqueda.toString());
		return ResponseEntity
					.ok()
					.body(busqueda);
	}
	
//	============================GET ALL FILE METHOD==============================
//	CON {---:.+} INDICAMOS QUE filename TIENE UNA EXTENSION (DIFERNETE SEGUN EL TIPO DE IMG)
	@GetMapping("/loadAll")
	@ApiOperation(value = "Cargamos todas las imagenes.",
				  notes = "Aqui devolvemos todas las imagenes en base64.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Imagenes convertidas y enviadas."),
			@ApiResponse(responseCode = "404", description = "Imagenes no convertidas.")
	})
	public ResponseEntity<Response> loadAll() throws Exception{
		log.info("He entrado a loadAll.");
		
		List<Resource> allResources = service.loadAll();
		List<String> allImages = new ArrayList<>();
		List<String> allNames = new ArrayList<>();
		
		for (Resource resource:allResources) {
			try {
				allNames.add(resource.getFilename());
				byte[] fileContent = FileUtils.readFileToByteArray(resource.getFile());
				String resultado = Base64.getEncoder().encodeToString(fileContent);
				allImages.add(resultado);
				
				log.info("Imagen cargada y codificada a String (base64).");
			} catch (Exception e) {
				
				log.error("Error al cargar la imagen, es posible que no este en la carpeta." + e);
			}
		}
		return ResponseEntity
				.ok()
				.body(new Response(allImages,allNames));
	}
}
