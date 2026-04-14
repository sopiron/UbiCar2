package com.uade.tpo.demo.controllers.cart;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long productId;
    private LocalDate date; 

}