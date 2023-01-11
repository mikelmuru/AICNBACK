package com.aicnfirst.model;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class InputReq {
    private MultipartFile file;
    private EncodedImage encodedImage;
    private String currentUser;
}
