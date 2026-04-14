package com.uade.tpo.demo.exceptions.user;

import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El usuario no es vendedor")
public class UserNotSellerException extends RuntimeException {
}