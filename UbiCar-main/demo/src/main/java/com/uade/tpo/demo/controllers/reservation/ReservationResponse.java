package com.uade.tpo.demo.controllers.reservation;

import java.time.LocalDate;

import com.uade.tpo.demo.entity.ReservationStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservationResponse {

    private Long id;
    private LocalDate date;

    // info del producto
    private Long productId;
    private String productTitle;
    private Double total;


    // info del usuario (el que reservó)
    private Long userId;
    private String userEmail;
    private ReservationStatus status;

}