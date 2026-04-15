package com.uade.tpo.demo.exceptions.product;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "El producto ya existe")
public class ProductAlreadyExistsException extends RuntimeException {
}
