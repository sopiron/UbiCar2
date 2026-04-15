package com.uade.tpo.demo.exceptions.product;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;


@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "No tienes permiso para realizar esta acción")
public class ProductUnauthorizedException extends RuntimeException {
}