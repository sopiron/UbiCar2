package com.uade.tpo.demo.controllers.blockedDate;

import java.time.LocalDate;

import lombok.Data;

@Data
public class BlockedDateRequest {
    private LocalDate date;
}
