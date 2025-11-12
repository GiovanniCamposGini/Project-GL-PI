package com.gl.project.entities.DTO;

import com.gl.project.entities.Order;
<<<<<<< HEAD
=======
import com.gl.project.entities.OrderItem;
>>>>>>> 264a16fbf826ee630aa2bbc602e7497b44616f1d
import com.gl.project.entities.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

<<<<<<< HEAD
public class OrderResponseDTO {
    private Long id;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private Double totalPrice;
    private UserResponseDTO user;
=======
import java.util.HashSet;
import java.util.Set;

public class OrderResponseDTO {
    private Long id;
    private OrderStatus status;
    private Double totalPrice;
    private UserResponseDTO user;
    private Set<OrderItem> Items;
>>>>>>> 264a16fbf826ee630aa2bbc602e7497b44616f1d

    public OrderResponseDTO(Order order) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.totalPrice = order.getTotalPrice();
        this.user = new UserResponseDTO(order.getUser());
<<<<<<< HEAD
=======
        this.Items = order.getItems();
>>>>>>> 264a16fbf826ee630aa2bbc602e7497b44616f1d
    }

    public Long getId() { return id; }
    public OrderStatus getStatus() { return status; }
    public Double getTotalPrice() { return totalPrice; }
    public UserResponseDTO getUser() { return user; }
<<<<<<< HEAD
=======
    public Set<OrderItem> getItems() { return Items; }

>>>>>>> 264a16fbf826ee630aa2bbc602e7497b44616f1d
}
