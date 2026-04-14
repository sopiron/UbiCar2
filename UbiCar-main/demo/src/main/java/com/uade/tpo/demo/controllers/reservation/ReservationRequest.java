package com.uade.tpo.demo.controllers.reservation;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ReservationRequest {
    
    private Long productId;
    private LocalDate date;
}
