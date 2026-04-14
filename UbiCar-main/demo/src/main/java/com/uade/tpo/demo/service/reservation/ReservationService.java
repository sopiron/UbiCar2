package com.uade.tpo.demo.service.reservation;

import java.util.List;

import com.uade.tpo.demo.controllers.reservation.ReservationRequest;
import com.uade.tpo.demo.entity.Reservation;

public interface ReservationService {
    public List<Reservation> getReservationsByUserId(Long userId);
    
    public Reservation getReservationById(Long id);
    
    public Reservation createReservation(ReservationRequest request);
    
    public Reservation payReservation(Long id);

    public Reservation cancelReservation(Long id);
}
