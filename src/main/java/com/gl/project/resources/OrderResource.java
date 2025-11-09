package com.gl.project.resources;

import com.gl.project.entities.DTO.OrderResponseDTO;
import com.gl.project.entities.Order;
import com.gl.project.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
                .map(OrderResponseDTO::new) // usa o construtor que recebe Order
                .toList();
        return ResponseEntity.ok().body(orderResponseDTOS);
    }

    @PostMapping
    public ResponseEntity<Order> save(@RequestBody Order newOrder) {
        orderService.save(newOrder);
        return ResponseEntity.ok(newOrder);
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
