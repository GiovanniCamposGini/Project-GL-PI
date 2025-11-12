package com.gl.project.resources;

import com.gl.project.entities.DTO.OrderRequestDTO;
import com.gl.project.entities.DTO.OrderResponseDTO;
import com.gl.project.entities.Order;
import com.gl.project.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/orders")
public class OrderResource {

    private final OrderService orderService;

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
                .map(OrderResponseDTO::new)
                .toList();
        return ResponseEntity.ok(orderResponseDTOS);
    }

    @PostMapping
    public ResponseEntity<Order> save(@RequestBody OrderRequestDTO orderRequestDTO) {
        Order order = orderService.save(orderRequestDTO.getUserId(), orderRequestDTO.getItems());
        return ResponseEntity.ok(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id, @RequestBody Order newOrder) {
        Order order = orderService.updateStatus(id, newOrder);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id) {
        orderService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
