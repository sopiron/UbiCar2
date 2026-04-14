package com.uade.tpo.demo.controllers.product;

import lombok.Data;

@Data
public class ProductRequest {
    
    private String title;
    private String description;
    private Double price;
    private String address;
    private Boolean active;
    private Double discountPercentage;
    private Boolean discountActive;
    private String vehicleType; // después lo convertimos a enum
    private Long sellerId; // importante para relacionar el producto con el usuario que lo vende

}
