package com.uade.tpo.demo.controllers.user;

import com.uade.tpo.demo.entity.Role;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
}