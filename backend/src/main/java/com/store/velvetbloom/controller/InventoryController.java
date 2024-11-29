package com.store.velvetbloom.controller;

import com.store.velvetbloom.dto.InventoryStatsResponseDTO;
import com.store.velvetbloom.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventories")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public InventoryStatsResponseDTO getInventoryStats() {
        return inventoryService.getInventoryStats();
    }
}
