package com.bootforge.orderservcie.client;

import com.bootforge.orderservcie.dto.InventoryResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "inventory-service",
        url = "${services.inventory.url}"
)
public interface InventoryClient {
    @PostMapping("/api/v1/inventories/{productId}/reserve")
    InventoryResponse reserveInventory(
            @PathVariable @Positive Long productId,
            @RequestParam @NotNull @Positive Integer quantity);
}
