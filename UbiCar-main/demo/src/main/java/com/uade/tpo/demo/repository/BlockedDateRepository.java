package com.uade.tpo.demo.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.demo.entity.BlockedDate;

@Repository
public interface BlockedDateRepository extends JpaRepository<BlockedDate, Long> {

        List<BlockedDate> findByProductId(Long productId); 

        boolean existsByProductIdAndDate(Long productId, LocalDate date);

       Optional<BlockedDate> findByProductIdAndDate(Long productId, LocalDate date);

        void deleteByProductIdAndDate(Long productId, LocalDate date);


    
}
