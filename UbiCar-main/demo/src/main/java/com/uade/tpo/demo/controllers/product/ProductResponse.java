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
    private Double price;
    private VehicleType vehicleType;
    private Boolean active;
    private Long sellerId;
    private List<Long> imageIds;

}