package com.bootforge.paymentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class PaymentServiceApplication {
    static void main(String[] args) {
        SpringApplication.run(PaymentServiceApplication.class, args);
    }
}
