package com.uade.tpo.demo.service.user;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import com.uade.tpo.demo.controllers.user.ChangeActiveStatusRequest;
import com.uade.tpo.demo.controllers.user.UpdateUserRequest;
import com.uade.tpo.demo.controllers.user.UserResponse;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.Reservation;
import com.uade.tpo.demo.repository.ReservationRepository;
import com.uade.tpo.demo.entity.ReservationStatus;
import com.uade.tpo.demo.entity.Role;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.exceptions.cart.UserNotFoundException;
import com.uade.tpo.demo.repository.ProductRepository;
import com.uade.tpo.demo.repository.UserRepository;
import com.uade.tpo.demo.service.AuthenticationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private final PasswordEncoder passwordEncoder;

    //ver perfil de usuario
    public UserResponse getMyProfile() {
       User user = authenticationService.getCurrentUser();
        return userRepository.findByEmail(user.getEmail())
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        //return mapToResponse(user);
    }

    //validar que el usuario que ingreso coincida con el usuairo que se quiere ver o actualizar, sino lanzar error de acceso denegado
    //si el usuario es admin, puede ver o actualizar cualquier perfil
    // obtener compradores de un producto

    //actualizar perfil de usuario
    public UserResponse updateMyProfile(UpdateUserRequest request) {
        User user = authenticationService.getCurrentUser();

        if (request.getFirstname() != null) {
        user.setFirstName(request.getFirstname());
        }

        if (request.getLastname() != null) {
        user.setLastName(request.getLastname());
        }

        if (request.getEmail() != null) {
         user.setEmail(request.getEmail());
        }

        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword())); // 
        }

        return mapToResponse(userRepository.save(user));
    }


    //vendedores 

    //obetener vendedores
    public List<UserResponse> getSellers() {
        return userRepository.findByRole(Role.SELLER)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    //obetener vendedor por id
    public UserResponse getSellerById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (user.getRole() != Role.SELLER) {
            throw new RuntimeException("El usuario no es vendedor");
        }

        return mapToResponse(user);
    }

    //administradores

    //obetener usuarios (rol admin)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() != Role.ADMIN)
                .map(this::mapToResponse)
                .toList();
    }

    //obetener usuarios (rol usuario)
    public List<UserResponse> getUsers() {
        return userRepository.findByRole(Role.USER)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    //obetener usuario por id
     public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return mapToResponse(user);
    }

    //actualizar rol de usuario
    public UserResponse changeRole(Long id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setRole(role);

        return mapToResponse(userRepository.save(user));
    }

    //convierte el usuario de bd a un formato de respuesta--> oculta password
    private UserResponse mapToResponse(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .active(user.isActive())
        .role(user.getRole())
        .build();
    }

    @Transactional
    public UserResponse changeActiveStatus(Long id, ChangeActiveStatusRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setActive(request.isActive());

        if (Boolean.FALSE.equals(request.isActive()) && user.getRole() == Role.SELLER) {
            List<Product> products = productRepository.findBySellerId(user.getId());

            for (Product product : products) {
                product.setActive(false);
            }

            productRepository.saveAll(products);
        

            List<Reservation> reservations = reservationRepository
                        .findByProduct_Seller_Id(
                                user.getId()
                        );
                        System.out.println(
        "Reservas encontradas: "
        + reservations.size()
);

            LocalDate today = LocalDate.now();

            for (Reservation reservation : reservations) {
                boolean hasNotFinished =
                        !reservation
                                .getEndDate()
                                .isBefore(today);

                boolean isNotCancelled =
                        reservation.getStatus()
                                != ReservationStatus.CANCELLED;

                if (
                    hasNotFinished
                    && isNotCancelled
                ) {
                    reservation.setStatus(
                            ReservationStatus.CANCELLED
                    );
                }
            }

            reservationRepository.saveAll(reservations);
        }

        return mapToResponse(userRepository.save(user));
        }

     public void descuentoPrimeraCompraUsado(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());
        user.setPrimeraCompraRealizada(true);
        userRepository.save(user);
        }
}
    
