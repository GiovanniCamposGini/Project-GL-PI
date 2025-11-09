package com.gl.project.service;

import com.gl.project.entities.Order;
import com.gl.project.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    public OrderService(OrderRepository repository) {
        this.orderRepository = repository;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).get();
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }
    public Order updateStatus(Long id, Order order) {
        Order oldOrder = orderRepository.findById(id).get();
        oldOrder.setStatus(order.getStatus());
        return orderRepository.save(oldOrder);
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
}
