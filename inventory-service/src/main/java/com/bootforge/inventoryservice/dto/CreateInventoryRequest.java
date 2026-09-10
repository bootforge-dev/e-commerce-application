package com.bootforge.inventoryservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record CreateInventoryRequest(

        @NotNull(message = "Product id is mandatory")
        @Positive(message = "Product ID must be greater than 0")
        Long productId,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be greater than 0")
        Integer quantity

) {
}
