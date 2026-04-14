package com.uade.tpo.demo.exceptions.cart;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El producto ya está en el carrito")
public class ProductInCartException extends RuntimeException {
}