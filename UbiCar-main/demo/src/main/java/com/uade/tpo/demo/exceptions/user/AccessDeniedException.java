package com.uade.tpo.demo.exceptions.user;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Acceso denegado")
public class AccessDeniedException extends RuntimeException {
}