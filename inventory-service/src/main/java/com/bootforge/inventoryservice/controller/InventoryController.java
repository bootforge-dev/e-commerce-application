package com.bootforge.inventoryservice.controller;

import com.bootforge.inventoryservice.dto.CreateInventoryRequest;
import com.bootforge.inventoryservice.dto.InventoryAvailabilityResponse;
import com.bootforge.inventoryservice.dto.InventoryResponse;
import com.bootforge.inventoryservice.service.InventoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(
            @RequestBody @Valid CreateInventoryRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(inventoryService.createInventory(request));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getInventoryByProductId(
            @PathVariable @Positive Long productId) {
        return ResponseEntity
                .ok(inventoryService.getInventoryByProductId(productId));
    }

    @GetMapping("/{productId}/availability")
    public ResponseEntity<InventoryAvailabilityResponse> checkInventoryAvailability(
            @PathVariable @Positive Long productId,
            @RequestParam @NotNull @Positive Integer quantity) {
        return ResponseEntity
                .ok(inventoryService.checkInventoryAvailability(productId, quantity));
    }


}
