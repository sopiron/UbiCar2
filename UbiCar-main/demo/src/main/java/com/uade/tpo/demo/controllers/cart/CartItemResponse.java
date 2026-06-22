package com.uade.tpo.demo.controllers.cart;

import java.time.LocalDate;

import com.uade.tpo.demo.controllers.product.ProductResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponse {
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProductResponse product;
}