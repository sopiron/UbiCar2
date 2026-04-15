package com.uade.tpo.demo.service.productImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.entity.Image;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.Role;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.exceptions.cart.ProductNotFoundException;
import com.uade.tpo.demo.exceptions.image.ImageNotFoundException;
import com.uade.tpo.demo.exceptions.image.ImageUnauthorizedException;
import com.uade.tpo.demo.repository.ImageRepository;
import com.uade.tpo.demo.repository.ProductRepository;
import com.uade.tpo.demo.service.AuthenticationService;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public Image create(Image image, Long productId) {

        User currentUser = authenticationService.getCurrentUser();
    
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        
        if (currentUser.getRole() != Role.SELLER) {
            throw new ImageUnauthorizedException();
        }

        if (!product.getSeller().getId().equals(currentUser.getId())) {
            throw new ImageUnauthorizedException();
        }
        if (imageRepository.existsByProductId(productId)) {
        throw new RuntimeException("This product already has an image");
        }

        image.setProduct(product);   
 
        return imageRepository.save(image);

    }

    @Override
    public Image viewById(long id) {
        return imageRepository.findById(id)
                .orElseThrow(ImageNotFoundException::new);
    }

    @Override
    public void deleteImage(Long id) {
        if (!imageRepository.existsById(id)) {
            throw new RuntimeException("Image not found with id: " + id);
        }
        imageRepository.deleteById(id);
    }
}