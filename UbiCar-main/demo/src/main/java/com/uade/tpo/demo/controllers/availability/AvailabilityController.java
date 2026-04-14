package com.uade.tpo.demo.controllers.availability;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("availability")
public class AvailabilityController {
    
    @GetMapping("/dates") // Obtenemos las fechas de disponibilidad de un producto
    public String getAvailabilityDates(@RequestParam String param) {
        return new String();
    }
    

    @GetMapping("/blocked-dates") // Obtenemos las fechas bloqueadas de un producto
    public String getBlockedDates(@RequestParam String param) {
        return new String();
    }
    

    @PostMapping("/blocked-dates") // Se agrega a la lista de fechas bloqueadas de un producto
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    

    @DeleteMapping("/blocked-dates/{blockedDateId}") // Eliminamos las fechas bloqueadas de un producto
    public void deleteBlockedDates(@RequestParam String param) {
    }

}
