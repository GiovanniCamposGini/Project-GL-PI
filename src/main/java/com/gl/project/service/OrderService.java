package com.gl.project.service;

import com.gl.project.entities.*;
import com.gl.project.entities.DTO.OrderItemDTO;
import com.gl.project.repository.OrderItemRepository;
import com.gl.project.repository.OrderRepository;
import com.gl.project.repository.ProductRepository;
import com.gl.project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        ProductRepository productRepository,
                        OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + id));
    }

    public Order save(Long userID, Set<OrderItemDTO> items) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + userID));

        Order order = new Order(user, 0.0, OrderStatus.INPROGRESS);
        double total = 0.0;

        Set<OrderItem> orderItems = new HashSet<>();
        for (OrderItemDTO item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + item.getProductId()));

            OrderItem orderItem = new OrderItem(order, product, item.getQuantity(), product.getPrice());
            orderItems.add(orderItem);
            total += product.getPrice() * item.getQuantity();
        }

        order.setStatus(OrderStatus.INPROGRESS);
        order.setTotalPrice(total);
        order.setItems(orderItems);

        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        return order;
    }

    public Order updateStatus(Long id, Order order) {
        Order oldOrder = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + id));
        oldOrder.setStatus(order.getStatus());
        return orderRepository.save(oldOrder);
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
}
