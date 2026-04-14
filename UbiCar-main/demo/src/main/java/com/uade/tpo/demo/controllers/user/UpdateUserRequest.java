package com.uade.tpo.demo.controllers.user;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String firstname;
    private String lastname;
    private String password;
}