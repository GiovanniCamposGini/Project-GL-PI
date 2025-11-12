package com.gl.project.service;

<<<<<<< HEAD
import com.gl.project.entities.Order;
import com.gl.project.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
=======
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
>>>>>>> 264a16fbf826ee630aa2bbc602e7497b44616f1d

@Service
public class OrderService {

    private OrderRepository orderRepository;
<<<<<<< HEAD

    public OrderService(OrderRepository repository) {
        this.orderRepository = repository;
=======
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository repository , UserRepository userRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = repository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
>>>>>>> 264a16fbf826ee630aa2bbc602e7497b44616f1d
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).get();
    }

<<<<<<< HEAD
    public Order save(Order order) {
        return orderRepository.save(order);
=======
    public Order save(Long userID, Set<OrderItemDTO> items) {
        User user = userRepository.findById(userID)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + userID));
        Order order = new Order(user, 0.0, OrderStatus.INPROGRESS);
        double total = 0.0;

        Set<OrderItem> orderItems = new HashSet<>();
        for (OrderItemDTO item : items) {
            Product product = productRepository.findById(item.getProductId()).get();

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

>>>>>>> 264a16fbf826ee630aa2bbc602e7497b44616f1d
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
