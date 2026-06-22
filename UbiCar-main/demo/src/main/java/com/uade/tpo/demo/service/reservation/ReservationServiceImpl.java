package com.uade.tpo.demo.service.reservation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
import com.uade.tpo.demo.repository.BlockedDateRepository;
import com.uade.tpo.demo.repository.ProductRepository;
import com.uade.tpo.demo.repository.ReservationRepository;
import com.uade.tpo.demo.service.AuthenticationService;
import com.uade.tpo.demo.service.user.UserService;

import jakarta.transaction.Transactional;

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

    @Autowired
    private UserService userService;


    private ReservationResponse toResponse(Reservation reservation) {
        return ReservationResponse.builder()
            .id(reservation.getId())
            .productId(reservation.getProduct().getId())
            .productTitle(reservation.getProduct().getTitle())
            .startDate(reservation.getStartDate())
            .endDate(reservation.getEndDate())
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

       Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new RuntimeException("Product not found")); //TODO: implement this method to get the product by id

        if(!product.isActive()) {
            throw new RuntimeException("El producto no está activo");
        }

         LocalDate current = request.getStartDate();
        while (!current.isAfter(request.getEndDate())) {
            if (blockedDateRepository.existsByProductIdAndDate(request.getProductId(), current)) {
                throw new RuntimeException("El producto no está disponible el " + current);
            }
            current = current.plusDays(1);
        }

        if(user.getId().equals(product.getSeller().getId())) {
            throw new RuntimeException("No puedes reservar tu propio producto");
        }

        // calcula total x dias
        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
        double total = product.getPrice() * days;
        
        Reservation reservation = Reservation.builder()
            .user(user)
            .product(product)
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .total(total)
            .status(ReservationStatus.PENDING)
            .build();

        LocalDate day = request.getStartDate();
        while (!day.isAfter(request.getEndDate())) {
            BlockedDate blockedDate = BlockedDate.builder()
                    .product(product)
                    .date(day)
                    .build();
            blockedDateRepository.save(blockedDate);
            day = day.plusDays(1);
        }


        return toResponse(reservationRepository.save(reservation));
    }


    @Transactional
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

        if (!user.isPrimeraCompraRealizada()) {
            reservation.setTotal(reservation.getTotal() * 0.85);
            userService.descuentoPrimeraCompraUsado(user.getId());
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        return toResponse(reservationRepository.save(reservation));
    }

    @Transactional //Se hace todo lo del metodo o nada, si algo falla no se guarda nada en la base de datos
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

        LocalDate day = reservation.getStartDate();
                while (!day.isAfter(reservation.getEndDate())) {
                    blockedDateRepository.deleteByProductIdAndDate(reservation.getProduct().getId(), day);
                    day = day.plusDays(1);
                }        
        return toResponse(reservationRepository.save(reservation));
}
}