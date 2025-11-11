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


}
