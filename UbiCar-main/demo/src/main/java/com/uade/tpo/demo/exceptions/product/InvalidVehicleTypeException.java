package com.uade.tpo.demo.exceptions.product;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Tipo de vehículo inválido")
public class InvalidVehicleTypeException extends RuntimeException {
}
