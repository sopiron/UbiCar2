package com.uade.tpo.demo.controllers.product;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.service.product.ProductService;

import io.micrometer.core.ipc.http.HttpSender.Response;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("products")
public class ProductController {
    
    @Autowired
    private ProductService productService;


    //Listar productos

    @GetMapping ("/obtener/productosDisponibles")// Obtener productos disponibles en esa fecha
    public ResponseEntity<List<Product>> getAvailableProducts(@RequestParam LocalDate date) { //agregar excepcion por si no pone parametro
        return ResponseEntity.ok(productService.getAvailableProducts(date));
    }
    

    @GetMapping("/obtener/productosActivos") //Obtener todos los productos
    public ResponseEntity<List<Product>> getActiveProducts() {
        return ResponseEntity.ok(productService.getActiveProducts());
    }
    

    @GetMapping("/obtener/{id}") //Ver detalles de un producto por id
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {

        Optional<Product> product = productService.getProductById(id);
       
        return product
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
    }
    

    @PostMapping ("/crear")//Crear un nuevo producto
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest request) {
        
        return ResponseEntity.ok(productService.createProduct(request));
    }
    
    @PutMapping("/{id}") //Actualizar un producto por id
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateRequest request) {
        
        return ResponseEntity.ok(productService.updateProduct(id,request));
    }

    @PatchMapping("/{id}/active") //Actualizar estado de un producto por id
    public ResponseEntity<Product> updateProductState(@PathVariable Long id, @RequestBody ProductStatusRequest request) {
        return ResponseEntity.ok(productService.updateProductState(id,request));
    }

}
