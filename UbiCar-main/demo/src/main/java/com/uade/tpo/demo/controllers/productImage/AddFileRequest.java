package com.uade.tpo.demo.controllers.productImage;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class AddFileRequest {
    private String name;
    private MultipartFile file;
    private Long productId;
}
