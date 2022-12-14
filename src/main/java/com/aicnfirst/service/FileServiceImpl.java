package com.aicnfirst.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.aicnfirst.model.EncodedImage;
import com.aicnfirst.model.FilePersist;
import com.aicnfirst.model.Response;
import com.aicnfirst.repository.FilesRepository;

@Service
public class FileServiceImpl implements FileService{
	
	@Autowired
	private WebClient cliente;

	@Autowired
	private FilesRepository repository;
	
	private Logger log = LoggerFactory.getLogger(FileServiceImpl.class);
	private final Path DermaImages = Paths.get("/app/derma");
	
	@Override
//	COPIAMOS LOS BYTES DE FILE (ENTRADA) EN EL PATH NoSavedImgs CON EL NOMBRE ORIGINAL DEL ARCHIVO 
	public EncodedImage load2(int file) {
		log.info("He entrado a save. Busco la imagen con id: " + file);
		EncodedImage result = new EncodedImage();
		Optional<FilePersist> myFile = repository.findById(file);
		if (myFile.isPresent()) {
			result.setEncodedFile(myFile.get().getImageBytes());
			result.setImagename(myFile.get().getImageName());
			result.setResultado(myFile.get().getResult());
		} else {
			result.setResultado("No se ha encontrado la imagen.");
		}
		return result;
	}
	
	@Override
	public Resource load(String name) throws Exception {
		log.info("He entrado en load.");
		Path file = DermaImages.resolve(name);
		return new UrlResource(file.toUri());
	}
	
	@Override
	public List<Resource> loadAll() throws Exception {
		List<Resource> imagenes = new ArrayList<>();
		int i = 0;
		for (i=120;i<220;i++) {
			String name = "Dermatitis_" + i + ".jpg";
			Path file = DermaImages.resolve(name);
			Resource fileResource = new UrlResource(file.toUri());
			imagenes.add(fileResource);
		}
		return imagenes;
	}
	
	@Override
//	RECORREMOS LA LISTA DE IMGS Y ENVIAMOS CADA UNA DE ELLAS AL METODO SAVE PARA COPIARLAS INDIVIDUALMENTE
	public ArrayList<EncodedImage> saveAll(List<MultipartFile> files) throws Exception {
		log.info("He entrado a saveAll.");
		
		ArrayList<EncodedImage> imagenesResueltas = new ArrayList<>();
		
		for (MultipartFile file : files) {
			EncodedImage nuevaImagen = new EncodedImage();
			nuevaImagen.setEncodedFile(Base64.getEncoder().encodeToString(file.getBytes()));
			nuevaImagen.setResultado(pruebaIA(file).getResult());
			imagenesResueltas.add(nuevaImagen);
			log.info("Terminado bucle de procesamiento de imagenes.");

			FilePersist nuevoRegistro = new FilePersist();
			nuevoRegistro.setImageBytes(nuevaImagen.getEncodedFile());
			nuevoRegistro.setImageName(file.getOriginalFilename());
			nuevoRegistro.setResult(nuevaImagen.getResultado());
			log.info(nuevoRegistro.toString());
			repository.save(nuevoRegistro);
			log.info("Imagen registrada en la bbdd.");
		}
		
		return imagenesResueltas;
	}
	
	public Response pruebaIA(MultipartFile image) {
		
		log.info("Entro a la llamadaa a la IA: http://python/predict");
		
		return cliente.post()
				.uri("http://localhost:8081/predict")
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.accept(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromMultipartData(bodyToIa(image)))
				.retrieve().bodyToMono(Response.class).block();
	}
	
	public MultiValueMap<String, HttpEntity<?>> bodyToIa(MultipartFile image) {
		
		MultipartBodyBuilder builder = new MultipartBodyBuilder();
		builder.part("file", image.getResource());
		builder.part("model", "keras");
		return builder.build();
	}
}
