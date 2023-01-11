package com.aicnfirst.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.aicnfirst.model.EncodedImage;
import com.aicnfirst.model.InputReq;
import com.aicnfirst.model.Response;

public interface FileService {
	public List<EncodedImage> filterControl(String filter) throws Exception;
	
	public List<EncodedImage> searchByName(String name) throws Exception;
	
	public List<EncodedImage> searchById(int id) throws Exception;

	public List<EncodedImage> searchByResult(String resultType) throws Exception;

	public List<EncodedImage> searchAll() throws Exception;

	public List<EncodedImage> loadAll() throws Exception;
	
	public List<EncodedImage> saveAll(List<MultipartFile> files) throws Exception;

	public List<EncodedImage> tryApp(List<MultipartFile> files) throws Exception;

	public Optional<EncodedImage> deleteById(Integer id) throws Exception;
	
	public Response pruebaIA(@RequestPart(value = "image") MultipartFile image);
	
	public MultiValueMap<String, HttpEntity<?>> bodyToIa(MultipartFile image);

	// public String saveAuditable(InputReq request);

	public List<EncodedImage> updateAuditable(InputReq request);

}
