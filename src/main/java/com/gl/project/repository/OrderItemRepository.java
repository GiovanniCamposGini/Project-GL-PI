package com.gl.project.repository;


import com.gl.project.entities.OrderItem;
import com.gl.project.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
