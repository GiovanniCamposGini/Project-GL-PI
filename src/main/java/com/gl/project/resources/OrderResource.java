package com.gl.project.resources;

<<<<<<< HEAD
import com.gl.project.entities.DTO.OrderResponseDTO;
import com.gl.project.entities.Order;
=======
import com.gl.project.entities.DTO.OrderRequestDTO;
import com.gl.project.entities.DTO.OrderResponseDTO;
import com.gl.project.entities.Order;
import com.gl.project.entities.OrderItem;
>>>>>>> 264a16fbf826ee630aa2bbc602e7497b44616f1d
import com.gl.project.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD
=======
import java.util.Set;
>>>>>>> 264a16fbf826ee630aa2bbc602e7497b44616f1d

@RestController
@RequestMapping(value = "/orders")
public class OrderResource {

    private OrderService orderService;

    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderResponseDTO> findById(@PathVariable Long id) {
        Order order = orderService.findById(id);
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO(order);
        return ResponseEntity.ok(orderResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> findAll() {
        List<Order> orders = orderService.findAll();
        List<OrderResponseDTO> orderResponseDTOS = orders.stream()
<<<<<<< HEAD
                .map(OrderResponseDTO::new) // usa o construtor que recebe Order
=======
                .map(OrderResponseDTO::new)
>>>>>>> 264a16fbf826ee630aa2bbc602e7497b44616f1d
                .toList();
        return ResponseEntity.ok().body(orderResponseDTOS);
    }

    @PostMapping
<<<<<<< HEAD
    public ResponseEntity<Order> save(@RequestBody Order newOrder) {
        orderService.save(newOrder);
        return ResponseEntity.ok(newOrder);
=======
    public ResponseEntity<Order> save(@RequestBody OrderRequestDTO orderRequestDTO) {
        Order order = orderService.save(orderRequestDTO.getUserId(), orderRequestDTO.getItems());
        return ResponseEntity.ok(order);
>>>>>>> 264a16fbf826ee630aa2bbc602e7497b44616f1d
    }

    @PutMapping("{id}")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id, @RequestBody Order newOrder) {
        Order order = orderService.updateStatus(id, newOrder);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Order> deleteOrderById(@PathVariable Long id) {
        orderService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
