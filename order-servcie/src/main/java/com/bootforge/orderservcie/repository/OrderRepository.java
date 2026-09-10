package com.bootforge.orderservcie.repository;

import com.bootforge.orderservcie.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}