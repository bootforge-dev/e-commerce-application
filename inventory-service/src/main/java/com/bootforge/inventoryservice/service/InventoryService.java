package com.bootforge.inventoryservice.service;

import com.bootforge.inventoryservice.dto.CreateInventoryRequest;
import com.bootforge.inventoryservice.dto.InventoryAvailabilityResponse;
import com.bootforge.inventoryservice.dto.InventoryResponse;
import com.bootforge.inventoryservice.entity.Inventory;
import com.bootforge.inventoryservice.exception.ProductAlreadyExistsException;
import com.bootforge.inventoryservice.exception.ProductNotFoundException;
import com.bootforge.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryResponse createInventory(CreateInventoryRequest request) {
        if (inventoryRepository.existsByProductId(request.productId())) {
            throw new ProductAlreadyExistsException("Product already exists with the productID: " + request.productId());
        }

        Inventory inventory = Inventory.builder()
                .productId(request.productId())
                .quantity(request.quantity())
                .build();

        Inventory savedInventory = inventoryRepository.save(inventory);

        return toResponse(savedInventory);
    }

    private InventoryResponse toResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .id(inventory.getId())
                .productId(inventory.getProductId())
                .quantity(inventory.getQuantity())
                .reservedQuantity(inventory.getReservedQuantity())
                .availableQuantity(inventory.getQuantity() - inventory.getReservedQuantity())
                .build();
    }

    public InventoryResponse getInventoryByProductId(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId).orElseThrow(
                () -> new ProductNotFoundException("Product not found with the productId: " + productId)
        );
        return toResponse(inventory);
    }

    public InventoryAvailabilityResponse checkInventoryAvailability(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId).orElseThrow(
                () -> new ProductNotFoundException("Product not found with the productId: " + productId)
        );

        int availableQuantity = inventory.getQuantity() - inventory.getReservedQuantity();

        return InventoryAvailabilityResponse.builder()
                .productId(inventory.getProductId())
                .requestedQuantity(quantity)
                .available(availableQuantity >= quantity)
                .build();
    }
}
