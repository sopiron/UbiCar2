package com.uade.tpo.demo.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.VehicleType;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(value = "select p from Product p where p.active = true and p.id not in (select b.product.id from BlockedDate b where b.date = :date) ")
    List<Product> findAvailableProducts(@Param("date") LocalDate date); //Param date es la variable donde se va a guardar el valor dela fecha que se pasa (LocalDate date)

    List<Product> findByActiveTrue(); // No hace falta query xq spring lo hace solo

    List<Product> findByVehicleType(VehicleType type); // filtro por tipo

    Optional<Product> findByTitleAndAddressAndSellerId(String title, String address, Long sellerId); // para validar que no exista un producto con mismo titulo, direccion y vendedor
}
