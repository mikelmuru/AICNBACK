package com.aicnfirst.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.aicnfirst.model.EncodedImage;
import com.aicnfirst.model.Response;

public interface FileService {
	
	public EncodedImage load2(int file) throws Exception;
	
	public Resource load(String name) throws Exception;
	
	public ArrayList<EncodedImage> saveAll(List<MultipartFile> files) throws Exception;
	
	public Response pruebaIA(@RequestPart(value = "image") MultipartFile image);
	
	public MultiValueMap<String, HttpEntity<?>> bodyToIa(MultipartFile image);
	
	public List<Resource> loadAll() throws Exception;

}
