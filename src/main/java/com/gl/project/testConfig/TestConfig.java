package com.gl.project.testConfig;

import com.gl.project.entities.*;
import com.gl.project.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.parameters.P;

import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private OrderItemRepository orderItemRepository;

    public TestConfig(ProductRepository productRepository, CategoryRepository categoryRepository, OrderRepository orderRepository, UserRepository userRepository,  OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
    }



    @Override
    public void run(String... args) throws Exception {
        User user = new User("lenine", "leninadadad@gmail.com","123", UserGroups.ADMIN, "48500932805");
        userRepository.save(user);

        Order order = new Order(user, 100, OrderStatus.INPROGRESS);
        orderRepository.save(order);

        Product product = new Product("Tijolo", "Bloco de cerâmica usado para construção civil.",0.85,"https://example.com/images/tijolo.jpg" );
        productRepository.save(product);

        Category category = new Category(null,"Chinelo");
        categoryRepository.save(category);






    }
}
