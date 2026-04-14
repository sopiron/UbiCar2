package com.uade.tpo.demo.service.reservation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.controllers.reservation.ReservationRequest;
import com.uade.tpo.demo.entity.BlockedDate;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.Reservation;
import com.uade.tpo.demo.entity.ReservationStatus;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.repository.BlockedDateRepository;
import com.uade.tpo.demo.repository.ProductRepository;
import com.uade.tpo.demo.repository.ReservationRepository;
import com.uade.tpo.demo.service.AuthenticationService;

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

    public List<Reservation> getReservationsByUserId(Long userId) {
        return reservationRepository.findByUserId(userId); //TODO: implement this method to find reservations by user id
    }


    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Reservation not found")); //TODO: implement this method to find reservation by id
    }


    public Reservation createReservation(ReservationRequest request) {
       
       User user = authenticationService.getCurrentUser(); //TODO: implement this method to get the user from the token

       Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new RuntimeException("Product not found")); //TODO: implement this method to get the product by id

        if(!product.isActive()) {
            throw new RuntimeException("Product is not active");
        }

        boolean blocked = blockedDateRepository.existsByProductIdAndDate(request.getProductId(), request.getDate()); //TODO: implement this method to check if the date is blocked for the product

        if (blocked){
            throw new RuntimeException("Product is not available for the selected date");
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
        blockedDateRepository.save(blockedDate); //TODO: implement this method to save the blocked date


        return reservationRepository.save(reservation);
    }


    public Reservation payReservation(Long id) {
        User user = authenticationService.getCurrentUser(); //TODO: implement this method to get the user from the token

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
        return reservationRepository.save(reservation);
    }

    public Reservation cancelReservation(Long id) {
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
        return reservationRepository.save(reservation);
}
}