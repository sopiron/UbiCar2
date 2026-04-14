package com.uade.tpo.demo.service.blockedDate;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.controllers.blockedDate.BlockedDateRequest;
import com.uade.tpo.demo.entity.BlockedDate;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.repository.BlockedDateRepository;
import com.uade.tpo.demo.repository.ProductRepository;

@Service
public class BlockedDateServiceImpl implements BlockedDateService {

    @Autowired
    private BlockedDateRepository blockedDateRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<BlockedDate> getBlockedDates(Long productId) {
        
        productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        return blockedDateRepository.findByProductId(productId); 
    }

    public BlockedDate createBlockedDate(Long productId, BlockedDateRequest request) {
        
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found with id: " + productId)); 

        boolean exists = blockedDateRepository.existsByProductIdAndDate(productId, request.getDate()); 

        if (exists) {
            throw new RuntimeException("Blocked date already exists for product id: " + productId + " and date: " + request.getDate());
        }

        BlockedDate blockedDate = BlockedDate.builder()
            .product(product)
            .date(request.getDate())
            .build();

        return blockedDateRepository.save(blockedDate); 
    }

    public void deleteBlockedDate(Long productId, LocalDate date) {
       
        productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        BlockedDate blockedDate = blockedDateRepository.findByProductIdAndDate(productId, date).orElseThrow(() -> new RuntimeException("Blocked date not found for product id: " + productId + " and date: " + date));
       
        blockedDateRepository.delete(blockedDate);
    }


    
}
