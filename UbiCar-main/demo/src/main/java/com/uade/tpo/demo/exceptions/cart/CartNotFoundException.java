package com.uade.tpo.demo.exceptions.cart;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Carrito no encontrado")
public class CartNotFoundException extends RuntimeException {
}