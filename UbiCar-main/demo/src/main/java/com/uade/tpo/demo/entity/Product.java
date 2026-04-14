package com.uade.tpo.demo.entity;

import java.util.List;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true, length = 1000)
    private String description;

    @Column(nullable = true)
    private Double price;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private boolean active;

     private Double discountPercentage;

    @Column(nullable = true)
    private boolean discountActive;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private VehicleType vehicleType;

    @ManyToOne //FK
    @JoinColumn(name = "seller_id", nullable = true)
    private User seller;

    @OneToMany(mappedBy = "product")
    private List<ProductImage> images;

    @OneToMany(mappedBy = "product")
    private List<BlockedDate> blockedDates;

    @OneToMany(mappedBy = "product")
    private List<Reservation> reservations;

}
