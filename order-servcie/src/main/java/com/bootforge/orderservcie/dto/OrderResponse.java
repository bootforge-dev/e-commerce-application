package com.bootforge.orderservcie.dto;

import com.bootforge.orderservcie.entity.OrderStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record OrderResponse(
        Long id,
        Long productId,
        Integer quantity,
        BigDecimal price,
        BigDecimal totalAmount,
        OrderStatus status,
        LocalDateTime createdAt
) {
}