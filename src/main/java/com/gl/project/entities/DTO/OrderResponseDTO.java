package com.gl.project.entities.DTO;

import com.gl.project.entities.Order;
import com.gl.project.entities.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class OrderResponseDTO {
    private Long id;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private Double totalPrice;
    private UserResponseDTO user;

    public OrderResponseDTO(Order order) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.totalPrice = order.getTotalPrice();
        this.user = new UserResponseDTO(order.getUser());
    }

    public Long getId() { return id; }
    public OrderStatus getStatus() { return status; }
    public Double getTotalPrice() { return totalPrice; }
    public UserResponseDTO getUser() { return user; }
}
