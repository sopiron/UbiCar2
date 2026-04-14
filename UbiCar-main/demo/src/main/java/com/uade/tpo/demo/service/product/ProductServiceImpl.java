package com.uade.tpo.demo.service.product;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.controllers.product.ProductRequest;
import com.uade.tpo.demo.controllers.product.ProductStatusRequest;
import com.uade.tpo.demo.controllers.product.ProductUpdateRequest;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.entity.VehicleType;
import com.uade.tpo.demo.repository.ProductRepository;
import com.uade.tpo.demo.repository.UserRepository;
import com.uade.tpo.demo.service.AuthenticationService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

  
    public List<Product> getAvailableProducts(LocalDate date) {
        return productRepository.findAvailableProducts(date); 
    }


    public List<Product> getActiveProducts() {
        return productRepository.findByActiveTrue();
    }

   
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }


    public Product createProduct(ProductRequest request) {

       User seller = authenticationService.getCurrentUser();

       Optional<Product> existingProduct =
            productRepository.findByTitleAndAddressAndSellerId(
                    request.getTitle(),
                    request.getAddress(),
                    seller.getId()
            );

        if (existingProduct.isPresent()) {
            throw new RuntimeException("Ya existe un producto paar este vendedor");
        }

        Product product = Product.builder().title(request.getTitle())
                .description(request.getDescription())
                .price(request.getPrice())
                .address(request.getAddress())
                .active(request.getActive())
                .discountPercentage(request.getDiscountPercentage())
                .discountActive(request.getDiscountActive())
                .vehicleType(VehicleType.valueOf(request.getVehicleType().toUpperCase()))
                .seller(seller)
                .build();

        return productRepository.save(product);
    }


    public Product updateProduct(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        
            if (request.getTitle() != null) {
                product.setTitle(request.getTitle());
            }
            if (request.getDescription() != null) {
                product.setDescription(request.getDescription());
            }
            if (request.getPrice() != null) {
                product.setPrice(request.getPrice());
            }
            if (request.getAddress() != null) {
                product.setAddress(request.getAddress());
            }
            if (request.getDiscountPercentage() != null) {
                product.setDiscountPercentage(request.getDiscountPercentage());
            }
            if (request.getDiscountActive() != null) {
                product.setDiscountActive(request.getDiscountActive());
            }
            if (request.getVehicleType() != null) {
                product.setVehicleType(VehicleType.valueOf(request.getVehicleType().toUpperCase()));
            }

        return productRepository.save(product);
    }


    public Product updateProductState(Long id, ProductStatusRequest request) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setActive(request.getActive());
        return productRepository.save(product);
    }
}

