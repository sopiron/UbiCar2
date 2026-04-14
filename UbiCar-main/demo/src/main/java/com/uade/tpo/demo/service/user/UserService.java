package com.uade.tpo.demo.service.user;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uade.tpo.demo.controllers.user.UpdateUserRequest;
import com.uade.tpo.demo.controllers.user.UserResponse;
import com.uade.tpo.demo.entity.Role;

@Service
public interface UserService {

    // usuario logueado
    public UserResponse getMyProfile();

    public UserResponse updateMyProfile(UpdateUserRequest request);

    //usuarios (admin)
    public List<UserResponse> getAllUsers();

    public UserResponse getUserById(Long id);

    //vendedores
    public List<UserResponse> getSellers();

    public UserResponse getSellerById(Long id);

    public List<UserResponse> getUsers();

    //roles (admin)
    public UserResponse changeRole(Long id, Role role);
}
    
