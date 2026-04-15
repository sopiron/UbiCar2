package com.uade.tpo.demo.exceptions.image;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "No tienes permiso para modificar esta imagen")
public class ImageUnauthorizedException extends RuntimeException {
}