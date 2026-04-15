package com.uade.tpo.demo.controllers.blockedDate;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.entity.BlockedDate;
import com.uade.tpo.demo.service.blockedDate.BlockedDateService;

@RestController
@RequestMapping("products/{productId}/blocked-dates")
public class BlockedDateController {

    @Autowired
    private BlockedDateService blockedDateService;

    @GetMapping("/obtener")
    public ResponseEntity<List<BlockedDate>> getBlockedDates(@PathVariable Long productId) {
        return ResponseEntity.ok(blockedDateService.getBlockedDates(productId)); //TODO: implement this method to get blocked dates by product id); 
    }

    @PostMapping("/crear")
    public ResponseEntity<BlockedDate> createBlockedDate(@PathVariable Long productId, @RequestBody BlockedDateRequest request) {
        return ResponseEntity.ok(blockedDateService.createBlockedDate(productId, request)); //TODO: implement this method to create a blocked date
    }

    @DeleteMapping("/{date}/borrar")
    public ResponseEntity<Void> deleteBlockedDate(@PathVariable Long productId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        blockedDateService.deleteBlockedDate(productId, date); //TODO: implement this method to delete a blocked date by product id and date
        return ResponseEntity.noContent().build();
    }
    
}