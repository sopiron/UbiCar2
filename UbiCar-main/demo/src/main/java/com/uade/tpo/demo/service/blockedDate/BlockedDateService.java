package com.uade.tpo.demo.service.blockedDate;

import java.time.LocalDate;
import java.util.List;

import com.uade.tpo.demo.controllers.blockedDate.BlockedDateRequest;
import com.uade.tpo.demo.entity.BlockedDate;

public interface BlockedDateService {
    
    List<BlockedDate> getBlockedDates(Long productId); 

    BlockedDate createBlockedDate(Long productId, BlockedDateRequest request); 

    void deleteBlockedDate(Long productId, LocalDate date); 
}
