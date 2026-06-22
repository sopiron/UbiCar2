package com.uade.tpo.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.controllers.auth.AuthenticationRequest;
import com.uade.tpo.demo.controllers.auth.AuthenticationResponse;
import com.uade.tpo.demo.controllers.auth.RegisterRequest;
import com.uade.tpo.demo.controllers.config.JwtService;
import com.uade.tpo.demo.entity.Cart;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.exceptions.user.InvalidLoginException;
import com.uade.tpo.demo.repository.CartRepository;
import com.uade.tpo.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
        private final UserRepository repository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        @Autowired
        private CartRepository cartRepository;



        public AuthenticationResponse register(RegisterRequest request) {

                System.out.println("EMAIL: " + request.getEmail());
                System.out.println("FIRSTNAME: " + request.getFirstname());
                System.out.println("LASTNAME: " + request.getLastname());
                
                var user = User.builder()
                                .firstName(request.getFirstname())
                                .lastName(request.getLastname())
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(request.getRole())
                                .active(true)
                                .primeraCompraRealizada(false)
                                .build();

                repository.save(user);

                 Cart cart = new Cart();
                cart.setUser(user);
                cartRepository.save(cart);

                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .id(user.getId())
                                .firstname(user.getFirstName())
                                .lastname(user.getLastName())
                                .email(user.getEmail())
                                .role(user.getRole().name())
                                .primeraCompraRealizada(
                                user.isPrimeraCompraRealizada()
                                )
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
               try {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));
        } catch (BadCredentialsException e) {
                throw new InvalidLoginException();
        }

        var user = repository.findByEmail(request.getEmail())
                        .orElseThrow(InvalidLoginException::new);
                        
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .id(user.getId())
                                .firstname(user.getFirstName())
                                .lastname(user.getLastName())
                                .email(user.getEmail())
                                .role(user.getRole().name())
                                .primeraCompraRealizada(user.isPrimeraCompraRealizada())
                                .build();
        }
        public User getCurrentUser() {

                String email = SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

                return repository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                }

        public User getCurrentUserOrNull() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication == null || !authentication.isAuthenticated() || 
                    authentication.getPrincipal().equals("anonymousUser")) {
                    return null;
                }
                return (User) authentication.getPrincipal();
        }
}
