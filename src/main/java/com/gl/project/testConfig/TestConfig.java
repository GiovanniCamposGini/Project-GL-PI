package com.gl.project.testConfig;

import com.gl.project.entities.*;
import com.gl.project.repository.CategoryRepository;
import com.gl.project.repository.OrderRepository;
import com.gl.project.repository.ProductRepository;
import com.gl.project.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public TestConfig(ProductRepository productRepository, CategoryRepository categoryRepository, OrderRepository orderRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        User user = new User("39053344705", "João da Silva", "joao.silva@example.com", UserGroups.USER, "ATIVO", "senhaSegura123", "01001000", new ArrayList<>());
        userRepository.save(user);

        User user2 = new User("39053344705", "João da Silva", "user2@example.com", UserGroups.USER, "ATIVO", "senhaSegura123", "04382130", new ArrayList<>());
        userRepository.save(user2);

        Order order = new Order(user, 100, OrderStatus.INPROGRESS);
        orderRepository.save(order);


    }
}
