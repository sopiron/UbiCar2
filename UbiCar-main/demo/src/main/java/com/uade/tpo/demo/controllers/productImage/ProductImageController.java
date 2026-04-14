package com.uade.tpo.demo.controllers.productImage;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.uade.tpo.demo.service.productImage.ProductImageService;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/product-images")
public class ProductImageController {
    
    @Autowired
    private ProductImageService productImageService;

    @GetMapping("/{id}")
    public ResponseEntity<String> getImage(@PathVariable Long id) {
        String base64 = productImageService.getImage(id);
    
        return ResponseEntity.ok(base64);
    }


    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file, @RequestParam Long productId) {

        try {
            productImageService.uploadImage(file, productId);
            return ResponseEntity.ok("Imagen subida correctamente");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al subir imagen");
        }
    }
    

   @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable Long id) {

        productImageService.deleteImage(id);
        return ResponseEntity.ok("Imagen eliminada correctamente");
    }

    


}