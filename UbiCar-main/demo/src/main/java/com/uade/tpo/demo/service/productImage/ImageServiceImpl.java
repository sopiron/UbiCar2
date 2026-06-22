package com.uade.tpo.demo.service.productImage;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.Role;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.entity.Image;
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
                .orElseThrow(() -> new RuntimeException("Product not found"));

        image.setProduct(product);   

        if (!product.getSeller().getId().equals(currentUser.getId())) {
            throw new RuntimeException("No podés agregar imágenes a un producto que no es tuyo");
        }

        if (currentUser.getRole() != Role.SELLER) {
            throw new RuntimeException("Solo los vendedores pueden agregar imágenes");
        }

        return imageRepository.save(image);

    }

    @Override
    public Image viewById(long id) {
        return imageRepository.findById(id).get();
    }
}