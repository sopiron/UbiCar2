package com.uade.tpo.demo.service.blockedDate;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.controllers.blockedDate.BlockedDateRequest;
import com.uade.tpo.demo.entity.BlockedDate;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.ReservationStatus;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.repository.BlockedDateRepository;
import com.uade.tpo.demo.repository.ProductRepository;
import com.uade.tpo.demo.repository.ReservationRepository;
import com.uade.tpo.demo.service.AuthenticationService;

@Service
public class BlockedDateServiceImpl implements BlockedDateService {

    @Autowired
    private BlockedDateRepository blockedDateRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private AuthenticationService authenticationService;

    public List<BlockedDate> getBlockedDates(Long productId) {
        
        productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        return blockedDateRepository.findByProductId(productId); 
    }

    public BlockedDate createBlockedDate(Long productId, BlockedDateRequest request) {

         User currentUser = authenticationService.getCurrentUser();
        
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found with id: " + productId)); 

        if (!product.getSeller().getId().equals(currentUser.getId())) {
            throw new RuntimeException("No podés bloquear fechas de un producto que no es tuyo");
        }

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

        User currentUser = authenticationService.getCurrentUser();
       
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        BlockedDate blockedDate = blockedDateRepository.findByProductIdAndDate(productId, date).orElseThrow(() -> new RuntimeException("Blocked date not found for product id: " + productId + " and date: " + date));
       
         if (!product.getSeller().getId().equals(currentUser.getId())) {
        throw new RuntimeException("No podés modificar fechas de un producto que no es tuyo");
    }

        boolean hasConfirmedReservation = //busca si hay una reserva confirmada con esa fecha y ese producto no se agrega
                reservationRepository.existsByProduct_IdAndDateAndStatus(
                    productId,
                    date,
                    ReservationStatus.CONFIRMED
                );

            if (hasConfirmedReservation) {
                throw new RuntimeException("No se puede habilitar una fecha con una reserva confirmada");
}

        blockedDateRepository.delete(blockedDate);
    }


    
}