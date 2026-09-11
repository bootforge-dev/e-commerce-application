package com.bootforge.paymentservice.consumer;

import com.bootforge.paymentservice.event.OrderCreatedEvent;
import com.bootforge.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCreatedEventConsumer {
    private final PaymentService paymentService;

    @KafkaListener(
            topics = "order-created",
            groupId = "payment-service"
    )
    public void consumeOrderCreated(OrderCreatedEvent event){
        System.out.println("Received Order created event: "+event);
        paymentService.createPaymentFromOrder(event);
    }
}
