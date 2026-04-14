package com.uade.tpo.demo.controllers.reservation;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.entity.Reservation;
import com.uade.tpo.demo.service.reservation.ReservationService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping ("/user/{userId}") //Ver mis reservas
    public ResponseEntity<List<Reservation>> getReservationByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reservationService.getReservationsByUserId(userId));
    }
    
    @GetMapping("/{id}") //Ver detalle de una reserva
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }
    
    @PostMapping //Crear una reserva
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequest request) {
        
        return ResponseEntity.ok(reservationService.createReservation(request));
    }
    
    @PostMapping("/{id}/pay")
    public ResponseEntity<Reservation> payReservation(@PathVariable Long id) {
        
        return ResponseEntity.ok(reservationService.payReservation(id));
    }

    @PostMapping("{id}/cancel")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable Long id) {
        
        return ResponseEntity.ok(reservationService.cancelReservation(id));
    }
    
    
}
