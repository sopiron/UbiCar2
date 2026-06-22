package com.uade.tpo.demo.service.product;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties.Authentication;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.controllers.product.ProductDeleteRequest;
import com.uade.tpo.demo.controllers.product.ProductRequest;
import com.uade.tpo.demo.controllers.product.ProductResponse;
import com.uade.tpo.demo.controllers.product.ProductStatusRequest;
import com.uade.tpo.demo.controllers.product.ProductUpdateRequest;
import com.uade.tpo.demo.entity.Image;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.entity.VehicleType;
import com.uade.tpo.demo.exceptions.cart.ProductNotFoundException;
import com.uade.tpo.demo.repository.ImageRepository;
import com.uade.tpo.demo.repository.ProductRepository;
import com.uade.tpo.demo.repository.UserRepository;
import com.uade.tpo.demo.service.AuthenticationService;
import com.uade.tpo.demo.service.location.LocationService;

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

    @Autowired
    private LocationService locationService;



    private ProductResponse toResponse(Product product, boolean aplicarDescuentoPrimeraCompra) {
    
        List<Long> imageIds = imageRepository.findByProductId(product.getId()).stream().map(Image::getId).toList();

        Double finalPrice = product.getPrice();
        if (aplicarDescuentoPrimeraCompra) {
            finalPrice = finalPrice * 0.85;
        }

        // double[] coordinates = locationService.getCoordinatesFromAddress(
        // product.getAddress(),
        // product.getZone()
// );

    return ProductResponse.builder()
            .id(product.getId())
            .title(product.getTitle())
            .description(product.getDescription())
            .address(product.getAddress())
            .zone(product.getZone())
            .latitude(product.getLatitude())
            .longitude(product.getLongitude())
            .price(product.getPrice())
            .finalPrice(finalPrice)
            .vehicleType(product.getVehicleType())
            .active(product.isActive())
            .deleted(product.isDeleted())
            .sellerId(product.getSeller().getId())
            .sellerName(product.getSeller().getFirstName() + " " + product.getSeller().getLastName())
            .imageIds(imageIds)
            .build();
}


    public List<ProductResponse> getAvailableProductsBetweenDates(LocalDate startDate, LocalDate endDate){
        User user = authenticationService.getCurrentUserOrNull();
        boolean descuento = user != null && !user.isPrimeraCompraRealizada();
        return productRepository.findAvailableProductsBetweenDates(startDate, endDate)
            .stream()
            .filter(product -> !product.isDeleted())
            .map(product -> toResponse(product, descuento))
            .toList();
    }


  
    public List<ProductResponse> getAvailableProducts(LocalDate date) {
        User user = authenticationService.getCurrentUserOrNull();
        boolean descuento = user != null && !user.isPrimeraCompraRealizada();
        
         return productRepository.findAvailableProducts(date)
                .stream()
                .filter(product -> !product.isDeleted())
                .map(p -> toResponse(p, descuento))
                .toList();


    }


    public List<ProductResponse> getActiveProducts() {
        User user = authenticationService.getCurrentUserOrNull();
        boolean descuento = user != null && !user.isPrimeraCompraRealizada();
        return productRepository.findByActiveTrue()
                .stream()
                .filter(product -> !product.isDeleted())
                .filter(product -> product.isActive())
                .map(p -> toResponse(p, descuento))
                .toList();
    }

   
    public ProductResponse getProductById(Long id) {

        User user = authenticationService.getCurrentUserOrNull();
        boolean descuento = user != null && !user.isPrimeraCompraRealizada();

        Product product = productRepository
            .findById(id)
            .orElseThrow(ProductNotFoundException::new);

        if (product.isDeleted()) {
            throw new ProductNotFoundException();
        }
        return toResponse(
            product,
            descuento);

    }


    public List<ProductResponse> getProductsBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId)
            .stream()
            .filter(product -> !product.isDeleted())
            .filter(product -> product.isActive())
            .map(product -> toResponse(product, false))
            .toList();
    }

    public List<ProductResponse> getProductsByVehicleType(VehicleType vehicleType) {

        User user = authenticationService.getCurrentUserOrNull();
        boolean descuento = user != null && !user.isPrimeraCompraRealizada();
        return productRepository.findByVehicleType(vehicleType)
                .stream()
                .filter(product -> !product.isDeleted())
                .map(p -> toResponse(p, descuento))
                .toList();
    }

    public List<ProductResponse> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        User user = authenticationService.getCurrentUserOrNull();
        boolean descuento = user != null && !user.isPrimeraCompraRealizada();
        return productRepository.findByPriceBetween(minPrice, maxPrice)
                .stream()
                .filter(product -> !product.isDeleted())
                .map(p -> toResponse(p, descuento))
                .toList();
    }

    
    public ProductResponse createProduct(ProductRequest request) {

        double[] coordinates = locationService.getCoordinatesFromAddress(
                request.getAddress(),
                request.getZone()
        );

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
                .zone(request.getZone())
                .latitude(coordinates[0])
                .longitude(coordinates[1])
                .active(request.getActive())
                .deleted(false)
                .vehicleType(VehicleType.valueOf(request.getVehicleType().toUpperCase()))
                .seller(seller)
                .build();

        productRepository.save(product);

        return toResponse(product, false);
    }


    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {

        User seller = authenticationService.getCurrentUser();

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (!product.getSeller().getId().equals(seller.getId())) {
            throw new RuntimeException("No tienes permiso para actualizar este producto");
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
            if (request.getVehicleType() != null) {
                product.setVehicleType(VehicleType.valueOf(request.getVehicleType().toUpperCase()));
            }

             if (request.getZone() != null) {
                product.setZone(request.getZone());
            }

            if (request.getAddress() != null || request.getZone() != null) {
                double[] coordinates = locationService.getCoordinatesFromAddress(
                        product.getAddress(),
                        product.getZone()
                );

                product.setLatitude(coordinates[0]);
                product.setLongitude(coordinates[1]);
            }


        productRepository.save(product);
        return toResponse(product, false);
    }


    public ProductResponse updateProductState(Long id, ProductStatusRequest request) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        product.setActive(request.getActive());
        productRepository.save(product);
        return toResponse(product, false);
       
    }

    public ProductResponse updateProductDeleted(Long id, ProductDeleteRequest request) {
    Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

    product.setDeleted(request.getDeleted());

    productRepository.save(product);

    return toResponse(product, false);
}

    
}

