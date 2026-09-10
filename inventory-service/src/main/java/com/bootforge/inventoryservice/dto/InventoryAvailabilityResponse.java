package com.bootforge.inventoryservice.dto;

import lombok.Builder;

@Builder
public record InventoryAvailabilityResponse(
        Long productId,
        Integer requestedQuantity,
        boolean available
) {
}
