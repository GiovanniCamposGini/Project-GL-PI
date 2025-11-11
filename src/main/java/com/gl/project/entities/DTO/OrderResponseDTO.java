package com.gl.project.entities.DTO;

import com.gl.project.entities.Order;
import com.gl.project.entities.OrderItem;
import com.gl.project.entities.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.HashSet;
import java.util.Set;

public class OrderResponseDTO {
    private Long id;
    private OrderStatus status;
    private Double totalPrice;
    private UserResponseDTO user;
    private Set<OrderItem> Items;

    public OrderResponseDTO(Order order) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.totalPrice = order.getTotalPrice();
        this.user = new UserResponseDTO(order.getUser());
        this.Items = order.getItems();
    }

    public Long getId() { return id; }
    public OrderStatus getStatus() { return status; }
    public Double getTotalPrice() { return totalPrice; }
    public UserResponseDTO getUser() { return user; }
    public Set<OrderItem> getItems() { return Items; }

}
