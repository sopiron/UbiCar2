package com.uade.tpo.demo.exceptions.image;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "La imagen es inválida")
public class InvalidImageException extends RuntimeException {
}