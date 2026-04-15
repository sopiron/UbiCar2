package com.uade.tpo.demo.service.productImage;
import java.sql.Blob;

import org.springframework.stereotype.Service;

import com.uade.tpo.demo.entity.Image;
@Service
public interface ImageService {

    public Image create(Image image, Long productId);

    public Image viewById(long id);

    void deleteImage(Long id);
}