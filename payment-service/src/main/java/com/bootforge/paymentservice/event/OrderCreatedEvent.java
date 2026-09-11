package com.bootforge.paymentservice.event;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderCreatedEvent(
        Long orderId,
        Long productId,
        Integer quantity,
        BigDecimal amount
) {
}
