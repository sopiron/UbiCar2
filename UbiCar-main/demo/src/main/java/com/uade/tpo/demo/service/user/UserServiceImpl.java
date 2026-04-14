package com.uade.tpo.demo.service.user;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;


import com.uade.tpo.demo.controllers.user.UpdateUserRequest;
import com.uade.tpo.demo.controllers.user.UserResponse;
import com.uade.tpo.demo.entity.Role;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.repository.UserRepository;
import com.uade.tpo.demo.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;

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
        .role(user.getRole())
        .build();
    }

}
    
