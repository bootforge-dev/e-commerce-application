package com.bootforge.orderservcie.service;

import com.bootforge.orderservcie.client.InventoryClient;
import com.bootforge.orderservcie.client.ProductClient;
import com.bootforge.orderservcie.dto.CreateOrderRequest;
import com.bootforge.orderservcie.dto.OrderResponse;
import com.bootforge.orderservcie.dto.ProductResponse;
import com.bootforge.orderservcie.entity.Order;
import com.bootforge.orderservcie.entity.OrderStatus;
import com.bootforge.orderservcie.event.OrderCreatedEvent;
import com.bootforge.orderservcie.producer.OrderEventProducer;
import com.bootforge.orderservcie.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductClient productClient;
    private final InventoryClient inventoryClient;
    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {

        //get the product details
        ProductResponse product =
                productClient.getProductById(request.productId());

        //reserve inventory
        inventoryClient.reserveInventory(request.productId(), request.quantity());

        //calculate the total amount
        BigDecimal totalAmount =
                product.price()
                        .multiply(
                                BigDecimal.valueOf(request.quantity())
                        );

        //create order
        Order order = Order.builder()
                .productId(request.productId())
                .quantity(request.quantity())
                .price(product.price())
                .totalAmount(totalAmount)
                .status(OrderStatus.INVENTORY_RESERVED)
                .build();

        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(savedOrder.getId())
                .productId(savedOrder.getProductId())
                .quantity(savedOrder.getQuantity())
                .amount(savedOrder.getTotalAmount())
                .build();
        orderEventProducer.publishOrderCreated(event);

        return this.toResponse(savedOrder);
    }

    private OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .price(order.getPrice())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }

}
