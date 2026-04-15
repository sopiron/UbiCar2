package com.uade.tpo.demo.exceptions.image;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;


@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error al subir la imagen")
public class ImageUploadException extends RuntimeException {
}