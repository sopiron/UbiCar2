package com.uade.tpo.demo.controllers.productImage;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/product-images")
public class ProductImageController {
    

     @GetMapping // Obtenemos las imagenes de un producto
    public String getProductImage(@RequestParam String productId) {
        return new String();
    }

    @GetMapping ("/{productId}")// Obtenemos la imagen del producto por el id
    public String getProductImageById(@PathVariable String productId) {
        return new String();
    }


    @PostMapping // Creamos una nueva imagen para un producto
    public String createImage(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    

    @DeleteMapping("/{productId}") // Eliminamos la imagen de un producto por el id
    public void deleteProductImage(@PathVariable String productId) {
        
    }

    


}
