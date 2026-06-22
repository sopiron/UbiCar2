package com.uade.tpo.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.demo.entity.Reservation;
import com.uade.tpo.demo.entity.ReservationStatus;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);

     List<Reservation> findByProduct_Seller_Id(
            Long sellerId
    );

@Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reservation r WHERE r.product.id = :productId AND r.startDate <= :date AND r.endDate >= :date AND r.status = :status")
    boolean existsConfirmedReservationForDate(
        @Param("productId") Long productId, 
        @Param("date") LocalDate date, 
        @Param("status") ReservationStatus status);
    }
