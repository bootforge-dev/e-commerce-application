package com.bootforge.paymentservice.dto;

import com.bootforge.paymentservice.entity.PaymentMethod;
import com.bootforge.paymentservice.entity.PaymentStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PaymentResponse(
        Long id,
        Long orderId,
        BigDecimal amount,
        PaymentStatus status,
        PaymentMethod paymentMethod,
        String transactionId,
        LocalDateTime createdAt
) {
}