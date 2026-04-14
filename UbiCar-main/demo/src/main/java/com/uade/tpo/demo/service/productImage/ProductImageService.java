package com.uade.tpo.demo.service.productImage;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface ProductImageService {

    void uploadImage(MultipartFile file, Long productId) throws IOException;

    String getImage(Long imageId);

    void deleteImage(Long imageId);
}