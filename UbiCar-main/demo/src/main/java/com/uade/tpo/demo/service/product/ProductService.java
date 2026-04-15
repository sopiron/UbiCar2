package com.uade.tpo.demo.service.product;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.uade.tpo.demo.controllers.product.ProductRequest;
import com.uade.tpo.demo.controllers.product.ProductResponse;
import com.uade.tpo.demo.controllers.product.ProductStatusRequest;
import com.uade.tpo.demo.controllers.product.ProductUpdateRequest;
import com.uade.tpo.demo.entity.Product;

public interface ProductService {

 //   
    public List<ProductResponse> getAvailableProducts(LocalDate date);
    
    public List<ProductResponse> getActiveProducts();
    
//
    public ProductResponse getProductById(Long id);
    
    public List<ProductResponse> getProductsBySellerId(Long sellerId);

    public List<ProductResponse> getProductsByVehicleType(com.uade.tpo.demo.entity.VehicleType vehicleType);

    public List<ProductResponse> getProductsByPriceRange(Double minPrice, Double maxPrice);

    public ProductResponse createProduct( ProductRequest request);

    public ProductResponse updateProduct( Long id,  ProductUpdateRequest request);

    public ProductResponse updateProductState( Long id,  ProductStatusRequest request);
    
}