package com.uade.tpo.demo.controllers.productImage;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.entity.Image;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.service.productImage.ImageService;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("images")
public class ImagesController {
    @Autowired
    private ImageService imageService;

    @CrossOrigin // Permitir solicitudes desde cualquier origen
    @GetMapping("/obtener") // Obtener imagen por id
    public ResponseEntity<ImageResponse> displayImage(@RequestParam("id") long id) throws IOException, SQLException {
        Image image = imageService.viewById(id);
        String encodedString = Base64.getEncoder()
                .encodeToString(image.getImage().getBytes(1, (int) image.getImage().length()));
        return ResponseEntity.ok().body(ImageResponse.builder().file(encodedString).id(id).build());
    }

    @PostMapping("/agregar") // Agregar imagen a un producto
    public String addImagePost(AddFileRequest request) throws IOException, SerialException, SQLException {
        
        byte[] bytes = request.getFile().getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

            Image image = Image.builder()
            .image(blob)
            .product(Product.builder().id(request.getProductId()).build())
            .build();

        imageService.create(image,request.getProductId());
        return "created";
    }

    @DeleteMapping("/{id}") 
    public ResponseEntity<String> deleteImage(@PathVariable Long id) { 
        imageService.deleteImage(id); 
        return ResponseEntity.ok("Imagen eliminada correctamente"); }
}
