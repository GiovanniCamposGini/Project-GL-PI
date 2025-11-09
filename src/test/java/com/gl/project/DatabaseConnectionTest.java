package com.gl.project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void deveConectarComSucesso() {
        String result = jdbcTemplate.queryForObject("SELECT 'Conexão OK!'", String.class);
        assertEquals("Conexão OK!", result);
    }
}
