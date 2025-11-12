package com.gl.project.user_testIntegrated;

import com.gl.project.entities.User;
import com.gl.project.entities.UserGroups;
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
public class UserTest {

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

    @Test
    public void testValidUser() {
        User user = new User("João Silva", "joao@example.com", "senha123", UserGroups.USER, "39053344705");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Usuário válido não deve ter violações");

        User savedUser = userService.create(user);
        assertNotNull(savedUser.getId());
        assertEquals("João Silva", savedUser.getName());
    }

    @Test
    public void testPasswordTooShort() {
        User user = new User("João Silva", "joao@example.com", "123", UserGroups.USER, "39053344705");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Senha com menos de 6 dígitos deve ter violações");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Senha tem que ter no mínimo 6 dígitos")));

        assertThrows(Exception.class, () -> userService.create(user));
    }

    @Test
    public void testInvalidCPF() {
        User user = new User("João Silva", "joao@example.com", "senha123", UserGroups.USER, "123456789");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "CPF inválido deve ter violações");

        assertThrows(Exception.class, () -> userService.create(user));
    }

    @Test
    public void testValidCPF() {
        String cpfValido = "39053344705";
        User user = new User("João Silva", "joao@example.com", "senha123", UserGroups.USER, cpfValido);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "CPF válido não deve ter violações");

        User savedUser = userService.create(user);
        assertEquals(cpfValido, savedUser.getCPF());
    }

    @Test
    public void testInvalidEmail() {
        User user = new User("João Silva", "joaoexample.com", "senha123", UserGroups.USER, "39053344705");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Email inválido deve ter violações");

        assertThrows(Exception.class, () -> userService.create(user));
    }

    @Test
    public void testStatusBancoNotNull() {
        User user = new User("João Silva", "joao@example.com", "senha123", UserGroups.USER, "39053344705");
        user.setStatusBanco(null);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "StatusBanco nulo deve ter violações");

        assertThrows(Exception.class, () -> userService.create(user));
    }

    @Test
    public void testNameTooShort() {
        User user = new User("Jo", "joao@example.com", "senha123", UserGroups.USER, "39053344705");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Nome com menos de 3 caracteres deve ter violações");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Nome não pode ter menos que 3 caracteres")));

        assertThrows(Exception.class, () -> userService.create(user));
    }

    @Test
    public void testNameNotBlank() {
        User user = new User("", "joao@example.com", "senha123", UserGroups.USER, "39053344705");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Nome vazio deve ter violações");

        assertThrows(Exception.class, () -> userService.create(user));
    }
}
