package com.uade.tpo.demo.controllers.product;

import lombok.Data;

@Data
public class ProductUpdateRequest {  //no se pide: id,sellerId y active. Esto solo lo podra realizar el vendedor del mismo producto.
    private String title;
    private String description;
    private Double price;
    private String address;
    private String zone;
    private String vehicleType;
}
