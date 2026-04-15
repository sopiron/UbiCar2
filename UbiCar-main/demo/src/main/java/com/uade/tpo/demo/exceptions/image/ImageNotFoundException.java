package com.uade.tpo.demo.exceptions.image;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Imagen no encontrada")
public class ImageNotFoundException extends RuntimeException {
}