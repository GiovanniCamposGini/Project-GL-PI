package com.gl.project.order;

import com.gl.project.entities.Order;
import com.gl.project.entities.OrderStatus;
import com.gl.project.entities.User;
import com.gl.project.entities.UserGroups;
import com.gl.project.service.OrderService;
import com.gl.project.service.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
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

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private User createValidUser() {
        return userService.create(new User("Cliente Teste", "cliente@teste.com", "senha123", UserGroups.USER, "39053344705"));
    }

    @Test
    public void testValidOrder() {
        User user = createValidUser();
        Order order = new Order(user, 89.90, OrderStatus.INPROGRESS);

        Set<ConstraintViolation<Order>> violations = validator.validate(order);
        assertTrue(violations.isEmpty(), "Pedido válido não deve ter violações");

        Order savedOrder = orderService.save(order);
        assertNotNull(savedOrder.getId());
        assertEquals(OrderStatus.INPROGRESS, savedOrder.getStatus());
    }

    @Test
    public void testNullStatus() {
        User user = createValidUser();
        Order order = new Order(user, 89.90, null);

        Set<ConstraintViolation<Order>> violations = validator.validate(order);
        assertFalse(violations.isEmpty(), "Status nulo deve gerar violação");

        assertThrows(Exception.class, () -> orderService.save(order));
    }

    @Test
    public void testNegativeTotalPrice() {
        User user = createValidUser();
        Order order = new Order(user, -50.0, OrderStatus.INPROGRESS);

        Set<ConstraintViolation<Order>> violations = validator.validate(order);
        assertFalse(violations.isEmpty(), "Preço negativo deve gerar violação");

        assertThrows(Exception.class, () -> orderService.save(order));
    }

    @Test
    public void testNullUser() {
        Order order = new Order(null, 89.90, OrderStatus.INPROGRESS);

        Set<ConstraintViolation<Order>> violations = validator.validate(order);
        assertFalse(violations.isEmpty(), "Usuário nulo deve gerar violação");

        assertThrows(Exception.class, () -> orderService.save(order));
    }

    @Test
    public void testUpdateOrderStatus() {
        User user = createValidUser();
        Order order = new Order(user, 89.90, OrderStatus.INPROGRESS);
        Order savedOrder = orderService.save(order);

        savedOrder.setStatus(OrderStatus.COMPLETED);
        Order updatedOrder = orderService.updateStatus(savedOrder.getId(), savedOrder);

        assertEquals(OrderStatus.COMPLETED, updatedOrder.getStatus());
    }
}
