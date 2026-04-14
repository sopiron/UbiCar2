package com.uade.tpo.demo.service.productImage;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.ProductImage;
import com.uade.tpo.demo.repository.ProductImageRepository;
import com.uade.tpo.demo.repository.ProductRepository;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void uploadImage(MultipartFile file, Long productId) throws IOException {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        ProductImage image = new ProductImage();
        image.setImage(file.getBytes());
        image.setProduct(product);

        productImageRepository.save(image);
    }

    @Override
    public String getImage(Long imageId) {

        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));

        return Base64.getEncoder().encodeToString(image.getImage());
    }

    @Override
    public void deleteImage(Long imageId) {

        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));

        productImageRepository.delete(image);
    }
}