package com.gl.project.entities.DTO;

import java.util.Set;

public class OrderRequestDTO {
    private Long userId;
    private Set<OrderItemDTO> items;

    public Long getUserId() {
        return userId;
    }

    public Set<OrderItemDTO> getItems() {
        return items;
    }

    public OrderRequestDTO(Long userId, Set<OrderItemDTO> items) {
        this.userId = userId;
        this.items = items;
    }
}
