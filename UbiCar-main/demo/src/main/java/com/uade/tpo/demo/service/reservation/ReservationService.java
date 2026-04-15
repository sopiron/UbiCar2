package com.uade.tpo.demo.service.reservation;

import java.util.List;

import com.uade.tpo.demo.controllers.reservation.ReservationRequest;
import com.uade.tpo.demo.controllers.reservation.ReservationResponse;
import com.uade.tpo.demo.entity.Reservation;

public interface ReservationService {
    
    public List<ReservationResponse> getReservationsByUserId(Long userId);
    
    public ReservationResponse getReservationById(Long id);
    
    public ReservationResponse createReservation(ReservationRequest request);
    
    public ReservationResponse payReservation(Long id);

    public ReservationResponse cancelReservation(Long id);
}