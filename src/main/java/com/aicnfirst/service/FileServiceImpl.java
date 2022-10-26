package com.aicnfirst.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aicnfirst.model.EncodedImage;

@Service
public class FileServiceImpl implements FileService{
	
	private Logger log = LoggerFactory.getLogger(FileServiceImpl.class);
	private final Path SavedImgs = Paths.get("FotosGuardadas");
	
	@Override
//	COPIAMOS LOS BYTES DE FILE (ENTRADA) EN EL PATH NoSavedImgs CON EL NOMBRE ORIGINAL DEL ARCHIVO 
	public void save(MultipartFile file) throws Exception {
		log.info("He entrado a save.");
		
		Files.copy(file.getInputStream(), this.SavedImgs.resolve(file.getOriginalFilename()));
	}
	
	@Override
	public Resource load(String name) throws Exception {
		log.info("He entrado en load.");
		Path file = SavedImgs.resolve(name);
		return new UrlResource(file.toUri());
	}
	
	@Override
//	RECORREMOS LA LISTA DE IMGS Y ENVIAMOS CADA UNA DE ELLAS AL METODO SAVE PARA COPIARLAS INDIVIDUALMENTE
	public ArrayList<EncodedImage> saveAll(List<MultipartFile> files) throws Exception {
		log.info("He entrado a saveAll.");
		
		ArrayList<EncodedImage> imagenesResueltas = new ArrayList<>();
		
		for (MultipartFile file : files) {
			int random = (int)(Math.random()*10+1);
			
			byte[] fileContent = file.getBytes();
			String fileBytes = Base64.getEncoder().encodeToString(fileContent);
			
			EncodedImage nuevaImagen = new EncodedImage();
			if (random > 5) {
				nuevaImagen.setResultado("Maligno. ");
			} else {
				nuevaImagen.setResultado("Beningo. ");
			}
			nuevaImagen.setEncodedFile(fileBytes);
			
			try {
				this.save(file);
				nuevaImagen.setResultado(nuevaImagen.getResultado().concat("Imagen registrada."));
				log.info("La imagen "+file.getOriginalFilename()+" se a añadido.");
			} catch (Exception e) {
				nuevaImagen.setResultado(nuevaImagen.getResultado().concat("Error al registrar la imagen."));
				log.error("Error al añadir "+file.getOriginalFilename()+"."+e);
			}
			imagenesResueltas.add(nuevaImagen);
		}
		return imagenesResueltas;
	}
	
	@Override
	public Stream<Path> loadAll(MultipartFile file) throws Exception {
		return Files.walk(SavedImgs, 1).filter(path -> !path.equals(SavedImgs)).map(SavedImgs::relativize);
	}
}
