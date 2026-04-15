package com.uade.tpo.demo.service.product;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.controllers.product.ProductRequest;
import com.uade.tpo.demo.controllers.product.ProductResponse;
import com.uade.tpo.demo.controllers.product.ProductStatusRequest;
import com.uade.tpo.demo.controllers.product.ProductUpdateRequest;
import com.uade.tpo.demo.entity.Image;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.entity.VehicleType;
import com.uade.tpo.demo.exceptions.cart.ProductNotFoundException;
import com.uade.tpo.demo.exceptions.product.ProductAlreadyExistsException;
import com.uade.tpo.demo.exceptions.product.ProductUnauthorizedException;
import com.uade.tpo.demo.repository.ImageRepository;
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

    @Autowired
    private ImageRepository imageRepository;


    private ProductResponse toResponse(Product product) {
    
        List<Long> imageIds = imageRepository.findByProductId(product.getId()).stream().map(Image::getId).toList();

    return ProductResponse.builder()
            .id(product.getId())
            .title(product.getTitle())
            .description(product.getDescription())
            .price(product.getPrice())
            .vehicleType(product.getVehicleType())
            .active(product.isActive())
            .sellerId(product.getSeller().getId())
            .imageIds(imageIds)
            .build();
}



  
    public List<ProductResponse> getAvailableProducts(LocalDate date) {
        
        return productRepository.findAvailableProducts(date)
        .stream()
        .map(this::toResponse)
        .toList(); 


    }


    public List<ProductResponse> getActiveProducts() {
        return productRepository.findByActiveTrue()
        .stream()
        .map(this::toResponse)
        .toList();
    }

   
    public ProductResponse getProductById(Long id) {

        return toResponse(productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException()));
    }


    public List<ProductResponse> getProductsBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId)
        .stream()
        .map(this::toResponse)
        .toList();
    }

    public List<ProductResponse> getProductsByVehicleType(VehicleType vehicleType) {

        return productRepository.findByVehicleType(vehicleType)
        .stream()
        .map(this::toResponse)
        .toList();
    }

    public List<ProductResponse> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice)
        .stream()
        .map(this::toResponse)
        .toList();
    }

    
    public ProductResponse createProduct(ProductRequest request) {

       User seller = authenticationService.getCurrentUser();

       Optional<Product> existingProduct =
            productRepository.findByTitleAndAddressAndSellerId(
                    request.getTitle(),
                    request.getAddress(),
                    seller.getId()
            );

        if (existingProduct.isPresent()) {
            throw new ProductAlreadyExistsException(); 
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

        productRepository.save(product);

        return toResponse(product);
    }


    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {

        User seller = authenticationService.getCurrentUser();

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException());

        if (!product.getSeller().getId().equals(seller.getId())) {
            throw new ProductUnauthorizedException();
        }
        
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

        productRepository.save(product);
        return toResponse(product);
    }


    public ProductResponse updateProductState(Long id, ProductStatusRequest request) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException());
        product.setActive(request.getActive());
        productRepository.save(product);
        return toResponse(product);
       
    }

    
}