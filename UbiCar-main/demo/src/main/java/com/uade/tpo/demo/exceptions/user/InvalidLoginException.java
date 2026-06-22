package com.uade.tpo.demo.exceptions.user;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El mail o la contraseña son incorrectos")
public class InvalidLoginException extends RuntimeException {
} 
