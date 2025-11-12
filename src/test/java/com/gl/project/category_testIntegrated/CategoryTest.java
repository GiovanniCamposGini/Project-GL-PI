package com.gl.project.category_testIntegrated;

import com.gl.project.entities.Category;
import com.gl.project.service.CategoryService;
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
public class CategoryTest {

    @Autowired
    private CategoryService categoryService;

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
    public void testValidCategory() {
        Category category = new Category(null, "Infantil");

        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertTrue(violations.isEmpty(), "Categoria válida não deve ter violações");

        Category saved = categoryService.create(category);
        assertNotNull(saved.getId());
        assertEquals("Infantil", saved.getName());
    }

    @Test
    public void testBlankName() {
        Category category = new Category(null, "");

        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertFalse(violations.isEmpty(), "Nome em branco deve gerar violação");

        assertThrows(Exception.class, () -> categoryService.create(category));
    }

    @Test
    public void testNameTooShort() {
        Category category = new Category(null, "In");

        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertFalse(violations.isEmpty(), "Nome com menos de 3 caracteres deve gerar violação");

        assertThrows(Exception.class, () -> categoryService.create(category));
    }
}
