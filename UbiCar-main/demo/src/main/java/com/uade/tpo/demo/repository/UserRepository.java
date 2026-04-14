package com.uade.tpo.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.demo.entity.Role;
import com.uade.tpo.demo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //uscar usuario por email 
    Optional<User> findByEmail(String email);

    // obtener usuarios por rol (SELLER, BUYER, ADMIN)
    List<User> findByRole(Role role);
}