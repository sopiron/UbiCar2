package com.uade.tpo.demo.service.reservation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.controllers.reservation.ReservationRequest;
import com.uade.tpo.demo.controllers.reservation.ReservationResponse;
import com.uade.tpo.demo.entity.BlockedDate;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.Reservation;
import com.uade.tpo.demo.entity.ReservationStatus;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.exceptions.cart.ProductNotFoundException;
import com.uade.tpo.demo.repository.BlockedDateRepository;
import com.uade.tpo.demo.repository.ProductRepository;
import com.uade.tpo.demo.repository.ReservationRepository;
import com.uade.tpo.demo.service.AuthenticationService;
import com.uade.tpo.demo.service.product.ProductService;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BlockedDateRepository blockedDateRepository;

    @Autowired
    private AuthenticationService authenticationService;


    private ReservationResponse toResponse(Reservation reservation) {
        return ReservationResponse.builder()
            .id(reservation.getId())
            .productId(reservation.getProduct().getId())
            .productTitle(reservation.getProduct().getTitle())
            .date(reservation.getDate())
            .total(reservation.getTotal())
            .userId(reservation.getUser().getId())
            .userEmail(reservation.getUser().getEmail())
            .status(reservation.getStatus())
            .build();
    }


    public List<ReservationResponse> getReservationsByUserId(Long userId) {
         return reservationRepository.findByUserId(userId)
        .stream()
        .map(this::toResponse)
        .toList(); 
    }


    public ReservationResponse getReservationById(Long id) {
        return toResponse(reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("La reserva no existe")));
    }


    public ReservationResponse createReservation(ReservationRequest request) {
       
       User user = authenticationService.getCurrentUser(); //TODO: implement this method to get the user from the token

       Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new ProductNotFoundException()); //TODO: implement this method to get the product by id

        if(!product.isActive()) {
            throw new RuntimeException("El producto no está activo");
        }

        boolean blocked = blockedDateRepository.existsByProductIdAndDate(request.getProductId(), request.getDate()); //TODO: implement this method to check if the date is blocked for the product

        if (blocked){
            throw new RuntimeException("El producto no está disponible para la fecha seleccionada");
        }

        if(user.getId().equals(product.getSeller().getId())) {
            throw new RuntimeException("No puedes reservar tu propio producto");
        }
       
        Reservation reservation = Reservation.builder()
            .user(user)
            .product(product)
            .date(request.getDate())
            .total(product.getPrice())
            .status(ReservationStatus.PENDING)
            .build();

        BlockedDate blockedDate = BlockedDate.builder()
            .product(product)
            .date(request.getDate())
            .build();
        blockedDateRepository.save(blockedDate); 


        return toResponse(reservationRepository.save(reservation));
    }


    public ReservationResponse payReservation(Long id) {
        User user = authenticationService.getCurrentUser(); 

        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only pay for your own reservations");
        }

        if(reservation.getStatus() == ReservationStatus.CONFIRMED) {
            throw new RuntimeException("Reservation is already payed");
        }

        if(reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new RuntimeException("You cannot pay for a cancelled reservation");
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        return toResponse(reservationRepository.save(reservation));
    }

    public ReservationResponse cancelReservation(Long id) {
        User user = authenticationService.getCurrentUser(); 

        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (!reservation.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only cancel your own reservations");
        }

        if(reservation.getStatus() == ReservationStatus.CONFIRMED) {
            throw new RuntimeException("You cannot cancel a confirmed reservation");
        }

        if(reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new RuntimeException("Reservation is already cancelled");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);

        blockedDateRepository.deleteByProductIdAndDate(reservation.getProduct().getId(), reservation.getDate()); 
        return toResponse(reservationRepository.save(reservation));
}
}