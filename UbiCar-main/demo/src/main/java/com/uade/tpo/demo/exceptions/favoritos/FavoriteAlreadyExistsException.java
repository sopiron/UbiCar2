package com.uade.tpo.demo.exceptions.favoritos;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "El producto ya está en favoritos")
public class FavoriteAlreadyExistsException extends RuntimeException {
}