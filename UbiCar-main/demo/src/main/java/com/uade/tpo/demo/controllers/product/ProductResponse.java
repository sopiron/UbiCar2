package com.uade.tpo.demo.controllers.product;

import java.util.List;

import com.uade.tpo.demo.entity.VehicleType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
    private Long id;
    private String title;
    private String description;
    private String address;
    private String zone;
    private Double latitude;
    private Double longitude;
    private Double price;
    private Double finalPrice; //precio final con descuento 
    private VehicleType vehicleType;
    private Boolean active;
    private Boolean deleted;
    private Long sellerId;
    private String sellerName;
    private List<Long> imageIds;

}