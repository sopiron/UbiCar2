package com.uade.tpo.demo.exceptions.favoritos;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "El producto no está en favoritos")
public class FavoriteNotFoundException extends RuntimeException {
}