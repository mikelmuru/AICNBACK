package com.aicnfirst.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
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
import com.aicnfirst.model.InputReq;
import com.aicnfirst.model.Response;
import com.aicnfirst.repository.FilesRepository;

@Service
public class FileServiceImpl implements FileService{
	
	@Autowired
	private WebClient cliente;

	@Autowired
	private FilesRepository repository;
	
	private Logger log = LoggerFactory.getLogger(FileServiceImpl.class);
	// private final Path DermaImages = Paths.get("/app/derma");
	private final Path DermaImages = Paths.get("./dermatitis_dataset_rename");

//	AQUI COPIAMOS LOS VALORES DE LA VARIABLE ENTIDAD A NUESTRA VARIABLE DTO
	private Optional<EncodedImage> convertFilePersistToEncodedImage(Optional<FilePersist> myFile) {
		Optional<EncodedImage> result = Optional.of(new EncodedImage());
		if (myFile.isPresent()) {
			result.get().setId(myFile.get().getId());
			result.get().setEncodedFile(myFile.get().getImageBytes());
			result.get().setImagename(myFile.get().getImageName());
			result.get().setResultado(myFile.get().getResult());
			result.get().setCreatedBy(myFile.get().getCreatedBy());
			result.get().setCreatedDate(myFile.get().getCreatedDate());
			result.get().setLastModBy(myFile.get().getLastModBy());
			result.get().setLastModDate(myFile.get().getLastModDate());
			log.info("busqueda completada");
			return result;
		} else {
			log.info("busqueda error");
			return null;
		}
	}
//	AQUI CONVERTIMOS UNA VARIABLE RESOURCE A NUESTRA VARIABLE DTO (SE UTILIZA PARA CARGAR LAS IMAGENES DE PRUEBA)
	private EncodedImage convertResourceToEncodedImage(Resource myResource) {
		EncodedImage result = new EncodedImage();

		try {
			result.setImagename(myResource.getFilename());
			byte[] fileContent = FileUtils.readFileToByteArray(myResource.getFile());
			String resultado = Base64.getEncoder().encodeToString(fileContent);
			result.setEncodedFile(resultado);
			result.setResultado("Imagen de prueba");
			
			log.info("Imagen cargada y codificada a String (base64).");
		} catch (Exception e) {
			
			log.error("Error al cargar la imagen, es posible que no este en la carpeta." + e);
		}
		return result;
	}

//	AQUI COMPROBAMOS LOS FILTROS QUE SE HAN ENVIADO CON LA PETICION GET
//	===================================================================
	@Override
	public List<EncodedImage> filterControl(String filter) throws Exception {
		try {
			Integer id = Integer.parseInt(filter);
			return searchById(id.intValue());
		} catch (NumberFormatException ex) {
			if (FilenameUtils.getExtension(filter) != "") {
				return searchByName(filter);
			} else {
				return searchByResult(filter);
			}
		}
	}

//	============================================================
//	AQUI TENEMOS LOS METODOS RELACIONADOS CON LAS PETICIONES GET
//	============================================================
	@Override
	public List<EncodedImage> searchByName(String name) throws Exception{
		log.info("He entrado a searchByName");
		List<EncodedImage> resultList = new ArrayList<>();

		try {
			Optional<FilePersist> myFile = repository.findByImageName(name);
			Optional<EncodedImage> result = convertFilePersistToEncodedImage(myFile);
			if (result.isPresent()) {
				resultList.add(result.get());
			}
		} catch (Exception e) {
			log.info(e.toString());
		}

		return resultList;
	}
	
	@Override
	public List<EncodedImage> searchById(int id) throws Exception {
		log.info("He entrado a searchById.");
		List<EncodedImage> resultList = new ArrayList<>();

		try {
			Optional<FilePersist> myFile = repository.findById(id);
			Optional<EncodedImage> result = convertFilePersistToEncodedImage(myFile);
			if (result.isPresent()) {
				resultList.add(result.get());
			}
		} catch (Exception e) {
			log.info(e.toString());
		}

		return resultList;
	}

	@Override
	public List<EncodedImage> searchByResult(String resultType) throws Exception {
		log.info("He entrado a searchByResult");
		List<EncodedImage> resultList = new ArrayList<>();
		
		List<FilePersist> myFiles = repository.findByResult(resultType);

		for (FilePersist file : myFiles) {
			Optional<EncodedImage> result = convertFilePersistToEncodedImage(Optional.of(file));
			if (result.isPresent()) {
				resultList.add(result.get());
			}
		}
		return resultList;
	}

	@Override
	public List<EncodedImage> searchAll() throws Exception {
		log.info("He entrado a searchAll");
		List<EncodedImage> resultList = new ArrayList<>();
		
		List<FilePersist> myFiles = repository.findAll();

		for (FilePersist file : myFiles) {
			Optional<EncodedImage> result = convertFilePersistToEncodedImage(Optional.of(file));
			if (result.isPresent()) {
				resultList.add(result.get());
			}
		}
		return resultList;
	}
	
	@Override
	public List<EncodedImage> loadAll() throws Exception {
		List<EncodedImage> imagenes = new ArrayList<>();

		int i = 0;
		for (i=120;i<220;i++) {
			String name = "Dermatitis_" + i + ".jpg";
			Path file = DermaImages.resolve(name);
			Resource fileResource = new UrlResource(file.toUri());

			imagenes.add(convertResourceToEncodedImage(fileResource));
		}
		return imagenes;
	}
	
//  ==========================================================
//	AQUI TENEMOS LOS METODOS RELACIONADOS CON LA PETICION POST
//	==========================================================
	@Override
	//	RECORREMOS LA LISTA DE IMGS Y ENVIAMOS CADA UNA DE ELLAS AL METODO SAVE PARA COPIARLAS INDIVIDUALMENTE
	public List<EncodedImage> saveAll(List<MultipartFile> files) throws Exception {
		log.info("He entrado a saveAll.");
		
		List<EncodedImage> imagenesResueltas = new ArrayList<>();
		
		for (MultipartFile file : files) {
			EncodedImage nuevaImagen = new EncodedImage();
			nuevaImagen.setEncodedFile(Base64.getEncoder().encodeToString(file.getBytes()));
			nuevaImagen.setResultado(pruebaIA(file).getResult());
			nuevaImagen.setImagename(file.getOriginalFilename());
			imagenesResueltas.add(nuevaImagen);
			log.info("Terminado bucle de procesamiento de imagenes.");

			FilePersist nuevoRegistro = new FilePersist();
			nuevoRegistro.setImageBytes(nuevaImagen.getEncodedFile());
			nuevoRegistro.setImageName(file.getOriginalFilename());
			nuevoRegistro.setResult(nuevaImagen.getResultado());
			log.info(nuevoRegistro.toString());
			try {
				FilePersist saved = repository.save(nuevoRegistro);
				nuevaImagen.setId(saved.getId());
			} catch (Exception ex) {
				nuevaImagen.setResultado(
					"Imagen ya registrada. Consulta su resultado en la bbdd.");
			}
			log.info("Imagen registrada en la bbdd.");
		}
		
		return imagenesResueltas;
	}

	@Override
	//	RECORREMOS LA LISTA DE IMGS Y ENVIAMOS CADA UNA DE ELLAS AL METODO SAVE PARA COPIARLAS INDIVIDUALMENTE
	public List<EncodedImage> tryApp(List<MultipartFile> files) throws Exception {
		log.info("He entrado a saveAll.");
		
		List<EncodedImage> imagenesResueltas = new ArrayList<>();
		
		for (MultipartFile file : files) {
			EncodedImage nuevaImagen = new EncodedImage();
			nuevaImagen.setEncodedFile(Base64.getEncoder().encodeToString(file.getBytes()));
			nuevaImagen.setResultado(pruebaIA(file).getResult());
			nuevaImagen.setImagename(file.getOriginalFilename());
			imagenesResueltas.add(nuevaImagen);
			log.info("Terminado bucle de procesamiento de imagenes.");
		}
		
		return imagenesResueltas;
	}
	
	public Response pruebaIA(MultipartFile image) {
		
		log.info("Entro a la llamadaa a la IA");
		
		return cliente.post()
				.uri("http://localhost:8081/predict")
				// .uri("http://python:8081/predict")
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

//  ======================================================
//	AQUI TENEMOS LOS METODOS RELACIONADOS A PETICIONES PUT
//	======================================================
	@Override
	public List<EncodedImage> updateAuditable(InputReq newdata) {
		List<EncodedImage> result= new ArrayList<>();

		FilePersist updatedFile = repository.findById(newdata.getEncodedImage().getId()).get();
		updatedFile.setImageName(newdata.getEncodedImage().getImagename());
		updatedFile.setDescription(newdata.getEncodedImage().getDescription());
		updatedFile.setLastModBy(newdata.getCurrentUser());

		result.add(convertFilePersistToEncodedImage(Optional.of(repository.save(updatedFile))).get());
		return result;
	}

//	===================================================
//	AQUI TENEMOS LOS METODOS PARA LAS PETICIONES DELETE
//	===================================================
	@Override
	public Optional<EncodedImage> deleteById(Integer id) {
		Optional<EncodedImage> result = Optional.of(new EncodedImage());
		Optional<FilePersist> deletedImage = repository.findById(id);

		if (!deletedImage.isPresent()) {
			result = null;
			return result;
		}

		result = convertFilePersistToEncodedImage(deletedImage);
		repository.deleteById(id);
		return result;
	}

//	PRUEBA DE SAVE CON EL DTO INPUTREQ PARA AUDITAR LA BBDD
	// @Override
	// public String saveAuditable(InputReq request) {
	// 	log.info(request.toString());

	// 	String currentUser = request.getCurrentUser();
	// 	FilePersist newFile = request.getFile();
	// 	newFile.setCreatedBy(currentUser);
	// 	log.info("Hasta antes del save todo ok.");
	// 	log.info(newFile.toString());
	// 	FilePersist saved = repository.save(newFile);
	// 	return saved.getCreatedBy();
	// }

	// @Override
	// public String updateAuditable(InputReq request) {
	// 	log.info(request.toString());
	// 	FilePersist fileToUpdate = repository.findById(request.getFile().getId()).get();

	// 	if (fileToUpdate != null) {
	// 		fileToUpdate.setLastModBy(request.getCurrentUser());
	// 		fileToUpdate.setImageName(request.getFile().getImageName());
	// 		fileToUpdate.setResult(request.getFile().getResult());
	// 		FilePersist saved = repository.save(fileToUpdate);
	// 		return saved.toString();
	// 	} else {
	// 		return "Error al actualizar, comprueba el id.";
	// 	}
	// }
}
