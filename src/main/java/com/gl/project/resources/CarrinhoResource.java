package com.gl.project.resources;

import com.gl.project.entities.DTO.AddItemRequest;
import com.gl.project.entities.DTO.OrderResponseDTO;
import com.gl.project.entities.Order;
import com.gl.project.entities.OrderStatus;
import com.gl.project.entities.User;
import com.gl.project.service.CarrinhoService;
import com.gl.project.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoResource {
    @Autowired
    private CarrinhoService carrinhoService;

    public CarrinhoResource(CarrinhoService orderService) {
        this.carrinhoService = orderService;
    }

    // Buscar carrinho do usuário logado
    @GetMapping
    public ResponseEntity<OrderResponseDTO> getCarrinho(@AuthenticationPrincipal User user) {
        Order carrinho = carrinhoService.findByUserAndStatus(user.getId(), OrderStatus.INPROGRESS);
        if (carrinho == null) {
            // Se não existir, retorna vazio
            return ResponseEntity.ok(new OrderResponseDTO(new Order(user, 0.0, OrderStatus.INPROGRESS)));
        }
        return ResponseEntity.ok(new OrderResponseDTO(carrinho));
    }

    // Adicionar item ao carrinho
    @PostMapping("/add")
    public ResponseEntity<OrderResponseDTO> addItem(@AuthenticationPrincipal User user,
                                                    @RequestBody AddItemRequest request) {
        Order carrinho = carrinhoService.addItemToCart(user.getId(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok(new OrderResponseDTO(carrinho));
    }

    // Finalizar carrinho (checkout)
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDTO> checkout(@AuthenticationPrincipal User user) {
        Order carrinho = carrinhoService.checkout(user.getId());
        return ResponseEntity.ok(new OrderResponseDTO(carrinho));
    }
}
