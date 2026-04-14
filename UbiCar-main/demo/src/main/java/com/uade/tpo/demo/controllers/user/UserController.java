package com.uade.tpo.demo.controllers.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.service.user.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;
    
    //Mi perfil
    @PreAuthorize("isAuthenticated()")  
    @GetMapping("user/obtener") //Obtener datos del usuario logueado
    public ResponseEntity<UserResponse> getMyProfile() {
        return ResponseEntity.ok(userService.getMyProfile());
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("user/actualizar") //Actualizar datos del usuario logueado
    public ResponseEntity<UserResponse> updateMyProfile(
            @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateMyProfile(request));
    }


    //Vendedores

    //obetener vendedores
    @PreAuthorize("hasAnyRole('SELLER','ADMIN')")
    @GetMapping("sellers")
    public ResponseEntity<List<UserResponse>> getSellers() {
        return ResponseEntity.ok(userService.getSellers());
    }

    //obetener vendedor por id
    @PreAuthorize("hasAnyRole('SELLER','ADMIN')") //el comprador tamien puede ver el perfil del vendedor
    @GetMapping("sellers/{id}")
    public ResponseEntity<UserResponse> getSellerById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getSellerById(id));
    }

    //Administradores

    //obetener administradores
    @PreAuthorize("hasRole('ADMIN')")
   @GetMapping("admin")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    //obetener usuario por id
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("admin/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    
    //actualizar rol de usuario 
    @PutMapping("admin/{id}/role")
    public ResponseEntity<UserResponse> changeRole(
        @PathVariable Long id,
        @RequestBody ChangeRoleRequest request) {

    return ResponseEntity.ok(
        userService.changeRole(id, request.getRole())
    );
    }

    //cambiar el estado de un usuario (activo/inactivo)

}
