package com.aicnfirst.service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.aicnfirst.model.EncodedImage;

public interface FileService {
	
	public void save(MultipartFile file) throws Exception;
	
	public Resource load(String name) throws Exception;
	
	public ArrayList<EncodedImage> saveAll(List<MultipartFile> files) throws Exception;
	
	public Stream<Path> loadAll(MultipartFile file) throws Exception;

}
