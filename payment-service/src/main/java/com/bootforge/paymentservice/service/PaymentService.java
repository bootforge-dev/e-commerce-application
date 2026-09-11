package com.bootforge.paymentservice.service;

import com.bootforge.paymentservice.dto.CreatePaymentRequest;
import com.bootforge.paymentservice.dto.PaymentResponse;
import com.bootforge.paymentservice.entity.Payment;
import com.bootforge.paymentservice.entity.PaymentMethod;
import com.bootforge.paymentservice.entity.PaymentStatus;
import com.bootforge.paymentservice.event.OrderCreatedEvent;
import com.bootforge.paymentservice.exceptin.PaymentAlreadyExistsException;
import com.bootforge.paymentservice.exceptin.PaymentNotFoundException;
import com.bootforge.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        if (paymentRepository.existsByOrderId(request.orderId())) {
            throw new PaymentAlreadyExistsException(
                    "Payment already exists for orderId: " + request.orderId()
            );
        }
        Payment payment = Payment.builder()
                .orderId(request.orderId())
                .amount(request.amount())
                .paymentMethod(request.paymentMethod())
                .status(PaymentStatus.PENDING)
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        return toResponse(savedPayment);
    }

    @Transactional
    public PaymentResponse processPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> new PaymentNotFoundException("Payment not found with id: " + paymentId)
        );

        if (payment.getStatus() != PaymentStatus.PENDING) {
            return toResponse(payment);
        }

        boolean paymentSuccessful = true;

        if (paymentSuccessful) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setTransactionId(
                    "TXN-" + UUID.randomUUID()
            );
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }

        Payment savedPayment = paymentRepository.save(payment);

        return toResponse(savedPayment);
    }

    @Transactional
    public PaymentResponse createPaymentFromOrder(OrderCreatedEvent event) {
        PaymentResponse payment;
        if (paymentRepository.existsByOrderId(event.orderId())) {
            payment = getPaymentByOrderId(event.orderId());
        } else {
            CreatePaymentRequest request = new CreatePaymentRequest(
                    event.orderId(),
                    event.amount(),
                    PaymentMethod.UPI
            );
            payment = createPayment(request);
        }

        return processPayment(payment.id());
    }

    public PaymentResponse getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> new PaymentNotFoundException("Payment not found with id: " + paymentId)
        );
        return toResponse(payment);
    }

    public PaymentResponse getPaymentByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(
                () -> new PaymentNotFoundException("Payment not found with order id: " + orderId)
        );
        return toResponse(payment);
    }

    private PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .paymentMethod(payment.getPaymentMethod())
                .transactionId(payment.getTransactionId())
                .createdAt(payment.getCreatedAt())
                .build();
    }

}
