package com.uade.tpo.demo.service.product;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.uade.tpo.demo.controllers.product.ProductRequest;
import com.uade.tpo.demo.controllers.product.ProductStatusRequest;
import com.uade.tpo.demo.controllers.product.ProductUpdateRequest;
import com.uade.tpo.demo.entity.Product;

public interface ProductService {

 //   
    public List<Product> getAvailableProducts(LocalDate date);
    
    public List<Product> getActiveProducts();
    
//
    public Optional<Product> getProductById(Long id);
    
//
    public Product createProduct( ProductRequest request);


    public Product updateProduct( Long id,  ProductUpdateRequest request);

    public Product updateProductState( Long id,  ProductStatusRequest request);
    
}
