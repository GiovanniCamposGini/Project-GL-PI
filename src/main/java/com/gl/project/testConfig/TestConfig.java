package com.gl.project.testConfig;

import com.gl.project.entities.*;
import com.gl.project.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    public TestConfig(ProductRepository productRepository,
                      CategoryRepository categoryRepository,
                      OrderRepository orderRepository,
                      UserRepository userRepository,
                      OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Usuário de teste
        User user = new User("lenine", "leninadadad@gmail.com", "1234567", UserGroups.ADMIN, "48500932805");
        user.setCep("04382130");
        userRepository.save(user);

        // Pedido de teste
        Order order = new Order(user, 100, OrderStatus.INPROGRESS);
        orderRepository.save(order);

        // Produtos e categoria de teste
        Product product1 = new Product("Chinelo 1", "Chinelo confortável para uso diário", 3.85, "https://example.com/images/chinelo1.jpg");
        Product product2 = new Product("Chinelo 2", "Chinelo leve e resistente", 8.33, "https://example.com/images/chinelo2.jpg");
        Product product3 = new Product("Chinelo 3", "Chinelo estiloso e moderno", 2.44, "https://example.com/images/chinelo3.jpg");

        Category category = new Category(null, "Chinelo");
        product1.getCategories().add(category);
        product2.getCategories().add(category);
        product3.getCategories().add(category);

        categoryRepository.save(category);
        productRepository.saveAll(Arrays.asList(product1, product2, product3));

        // Itens do pedido
        OrderItem od1 = new OrderItem(order, product1, 20, product1.getPrice());
        OrderItem od2 = new OrderItem(order, product2, 20, product2.getPrice());
        OrderItem od3 = new OrderItem(order, product3, 20, product3.getPrice());

        orderItemRepository.saveAll(Arrays.asList(od1, od2, od3));
    }
}
