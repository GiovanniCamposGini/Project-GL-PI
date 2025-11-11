
package com.gl.project.VCR;


import com.gl.project.entities.User;
import com.gl.project.repository.UserRepository;
import com.gl.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class VCRViaCEPServiceTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void deveGravarCassetteQuandoBuscarEnderecoEmModoRecord() {
        User user = new User();
        user.setName("Lenine");
        user.setEmail("lenine@example.com");
        user.setPassword("123456");
        user.setCep("01001000");
        userRepository.save(user);

        var response = userService.buscarEnderecoDoUser(user.getId(), true);

        assertNotNull(response);
        assertEquals("SP", response.getUf());

        File cassette = new File("vcr_cassettes/cep_" + user.getCep() + ".json");
        assertTrue(cassette.exists(), "Cassette deveria ter sido criado");
    }

    @Test
    void deveReproduzirCassetteQuandoBuscarEnderecoEmModoLoad() {
        User user = new User();
        user.setName("lenine");
        user.setEmail("lenine@example.com");
        user.setPassword("123456");
        user.setCep("04382130");
        userRepository.save(user);


        userService.buscarEnderecoDoUser(user.getId(), true);

        var response = userService.buscarEnderecoDoUser(user.getId(), false);

        assertNotNull(response);
        assertEquals("SP", response.getUf());
    }

    @Test
    void deveLancarExcecaoQuandoCepInvalido() {
        User user = new User();
        user.setName("lenine");
        user.setEmail("lenine@example.com");
        user.setPassword("123456");
        user.setCep("00000000");

        userRepository.save(user);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            userService.buscarEnderecoDoUser(user.getId(), true);
        });

        assertTrue(ex.getMessage().contains("Erro ao buscar endereço"));
    }
}
