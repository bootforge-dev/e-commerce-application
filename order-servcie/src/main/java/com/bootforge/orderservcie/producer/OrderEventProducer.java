package com.bootforge.orderservcie.producer;

import com.bootforge.orderservcie.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private static final String ORDER_CREATED_TOPIC = "order-created";
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public void publishOrderCreated(OrderCreatedEvent event) {
        kafkaTemplate.send(
                ORDER_CREATED_TOPIC,
                event.orderId().toString(),
                event
        ).whenComplete((result, ex) ->{
            if(ex != null){
                System.err.println("Kafka publish failed:");
                ex.printStackTrace();
                return;
            }
            System.out.println(
                    "Kafka publish SUCCESS: topic=" +
                            result.getRecordMetadata().topic() +
                            ", partition=" +
                            result.getRecordMetadata().partition() +
                            ", offset=" +
                            result.getRecordMetadata().offset()
            );
        });
    }

}
