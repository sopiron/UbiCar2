package com.uade.tpo.demo.exceptions.product;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Producto no encontrado")
public class ProductNotFoundException extends RuntimeException {
}