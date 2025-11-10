package com.gl.project.testConfig;

import com.gl.project.entities.*;
import com.gl.project.repository.CategoryRepository;
import com.gl.project.repository.OrderRepository;
import com.gl.project.repository.ProductRepository;
import com.gl.project.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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
        User user = new User("lenine", "leninadadad@gmail.com","1234567", UserGroups.ADMIN, "48500932805");
        userRepository.save(user);

        Order order = new Order(user, 100, OrderStatus.INPROGRESS);
        orderRepository.save(order);


    }
}
