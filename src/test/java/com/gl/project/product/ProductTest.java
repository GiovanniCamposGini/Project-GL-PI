package com.gl.project.product;

import com.gl.project.entities.Product;
import com.gl.project.service.ProductService;
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
public class ProductTest {

    @Autowired
    private ProductService productService;

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

    @Test
    public void testValidProduct() {
        Product product = new Product(null, "Chinelo Conforto", "Chinelo de borracha com palmilha anatômica", 39.90, "https://img.com/chinelo.jpg");

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertTrue(violations.isEmpty(), "Produto válido não deve ter violações");

        Product saved = productService.create(product);
        assertNotNull(saved.getId());
        assertEquals("Chinelo Conforto", saved.getName());
    }

    @Test
    public void testNameTooShort() {
        Product product = new Product(null, "Ch", "Chinelo de borracha com palmilha anatômica", 39.90, "https://img.com/chinelo.jpg");

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty(), "Nome com menos de 3 caracteres deve ter violações");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("mínimo 3 caracteres")));

        assertThrows(Exception.class, () -> productService.create(product));
    }

    @Test
    public void testDescriptionTooShort() {
        Product product = new Product(null, "Chinelo Conforto", "Muito bom", 39.90, "https://img.com/chinelo.jpg");

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty(), "Descrição com menos de 10 caracteres deve ter violações");

        assertThrows(Exception.class, () -> productService.create(product));
    }

    @Test
    public void testPriceNegative() {
        Product product = new Product(null, "Chinelo Conforto", "Chinelo de borracha com palmilha anatômica", -10.0, "https://img.com/chinelo.jpg");

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty(), "Preço negativo deve ter violações");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("valor positivo")));

        assertThrows(Exception.class, () -> productService.create(product));
    }

    @Test
    public void testPriceNull() {
        Product product = new Product(null, "Chinelo Conforto", "Chinelo de borracha com palmilha anatômica", null, "https://img.com/chinelo.jpg");

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty(), "Preço nulo deve ter violações");

        assertThrows(Exception.class, () -> productService.create(product));
    }

    @Test
    public void testImageUrlBlank() {
        Product product = new Product(null, "Chinelo Conforto", "Chinelo de borracha com palmilha anatômica", 39.90, "");

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty(), "URL da imagem em branco deve ter violações");

        assertThrows(Exception.class, () -> productService.create(product));
    }

    @Test
    public void testNameBlank() {
        Product product = new Product(null, "", "Chinelo de borracha com palmilha anatômica", 39.90, "https://img.com/chinelo.jpg");

        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty(), "Nome em branco deve ter violações");

        assertThrows(Exception.class, () -> productService.create(product));
    }
}
