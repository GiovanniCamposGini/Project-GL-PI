package com.gl.project.order_testIntegrated;

import com.gl.project.entities.*;
import com.gl.project.entities.DTO.OrderItemDTO;
import com.gl.project.repository.ProductRepository;
import com.gl.project.service.OrderService;
import com.gl.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
public class OrderTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductRepository productRepository;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("gl_project_db")
            .withUsername("containerdocker")
            .withPassword("sa");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.show-sql", () -> "true");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
    }

    private User createValidUser() {
        return userService.create(new User("Cliente Teste", "cliente@teste.com", "senha123", UserGroups.USER, "39053344705"));
    }

    private Product createValidProduct(String name, double price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setImgURL("texto generico imagem");
        product.setDescription("Description generic");

        return productRepository.save(product);
    }

    @Test
    public void testValidOrderWithItems() {
        User user = createValidUser();
        Product product1 = createValidProduct("Produto A", 10.0);
        Product product2 = createValidProduct("Produto B", 5.0);

        Set<OrderItemDTO> items = Set.of(
                new OrderItemDTO(product1.getId(), 2), // total 20
                new OrderItemDTO(product2.getId(), 3)  // total 15
        );

        Order savedOrder = orderService.save(user.getId(), items);

        assertNotNull(savedOrder.getId());
        assertEquals(OrderStatus.INPROGRESS, savedOrder.getStatus());
        assertEquals(35.0, savedOrder.getTotalPrice());
        assertEquals(2, savedOrder.getItems().size());
    }

    @Test
    public void testUpdateOrderStatus() {
        User user = createValidUser();
        Product product = createValidProduct("Produto C", 20.0);

        Set<OrderItemDTO> items = Set.of(new OrderItemDTO(product.getId(), 1));
        Order savedOrder = orderService.save(user.getId(), items);

        savedOrder.setStatus(OrderStatus.COMPLETED);
        Order updatedOrder = orderService.updateStatus(savedOrder.getId(), savedOrder);

        assertEquals(OrderStatus.COMPLETED, updatedOrder.getStatus());
    }

    @Test
    public void testSaveOrderWithInvalidUser() {
        Product product = createValidProduct("Produto D", 10.0);
        Set<OrderItemDTO> items = Set.of(new OrderItemDTO(product.getId(), 1));

        assertThrows(RuntimeException.class, () -> orderService.save(999L, items));
    }

    @Test
    public void testSaveOrderWithInvalidProduct() {
        User user = createValidUser();
        Set<OrderItemDTO> items = Set.of(new OrderItemDTO(999L, 1));

        assertThrows(Exception.class, () -> orderService.save(user.getId(), items));
    }
}
