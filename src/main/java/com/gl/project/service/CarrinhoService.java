package com.gl.project.service;

import com.gl.project.entities.*;
import com.gl.project.repository.OrderRepository;
import com.gl.project.repository.ProductRepository;
import com.gl.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarrinhoService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    public Order findByUserAndStatus(Long userId, OrderStatus status) {
        return orderRepository.findByUserIdAndStatus(userId, status);
    }

    public Order addItemToCart(Long userId, Long productId, int quantity) {
        Order carrinho = orderRepository.findByUserIdAndStatus(userId, OrderStatus.INPROGRESS);
        if (carrinho == null) {
            User user = userRepository.findById(userId).orElseThrow();
            carrinho = new Order(user, 0.0, OrderStatus.INPROGRESS);
            orderRepository.save(carrinho);
        }

        Product produto = productRepository.findById(productId).orElseThrow();
        OrderItem item = new OrderItem(carrinho, produto, quantity, produto.getPrice());
        carrinho.getItems().add(item);

        double total = carrinho.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
        carrinho.setTotalPrice(total);

        return orderRepository.save(carrinho);
    }

    public Order checkout(Long userId) {
        Order carrinho = orderRepository.findByUserIdAndStatus(userId, OrderStatus.INPROGRESS);
        if (carrinho == null) throw new RuntimeException("Carrinho vazio");
        carrinho.setStatus(OrderStatus.COMPLETED);
        return orderRepository.save(carrinho);
    }
}
