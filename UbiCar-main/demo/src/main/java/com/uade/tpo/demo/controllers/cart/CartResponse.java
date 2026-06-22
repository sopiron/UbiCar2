package com.uade.tpo.demo.controllers.cart;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartResponse {
    private Long id;
    private LocalDateTime expiresAt;
    private List<CartItemResponse> items;
}