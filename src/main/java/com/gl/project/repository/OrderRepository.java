package com.gl.project.repository;

import com.gl.project.entities.Order;
import com.gl.project.entities.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByUserIdAndStatus(Long userId, OrderStatus status);
}
