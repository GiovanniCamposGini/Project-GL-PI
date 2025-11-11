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
        User user = new User("lenine", "leninadadad@gmail.com","1234567", UserGroups.ADMIN, "48500932805");
        /*user.setCep("04382130");*/
        userRepository.save(user);

        Order order = new Order(user, 100, OrderStatus.INPROGRESS);
        orderRepository.save(order);

        Product product3 = new Product("chinelo", "chineladaaaaaaaaaaaaaaaa",3.85,"https://example.com/images/tijolo.jpg" );
        Product product2 = new Product("chinelo2", "chineladaaaaaaaaaaaaaaaa",8.33,"https://example.com/images/tijolo.jpg" );
        Product product = new Product("chinelo3", "chineladaaaaaaaaaaaaaaaaa",2.44,"https://example.com/images/tijolo.jpg" );
        Category category = new Category(null,"Chinelo");
        product.getCategories().add(category);
        product2.getCategories().add(category);
        product3.getCategories().add(category);

        categoryRepository.save(category);
        productRepository.saveAll(Arrays.asList(product3, product2, product));

        OrderItem od = new OrderItem(order, product, 20, product.getPrice());
        OrderItem od2 = new OrderItem(order, product2, 20, product.getPrice());
        OrderItem od3 = new OrderItem(order, product3, 20, product.getPrice());

        orderItemRepository.saveAll(Arrays.asList(od,od2,od3));

    }
}
