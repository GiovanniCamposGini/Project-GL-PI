package com.gl.project.stepDefinition;

import com.gl.project.entities.DTO.RegisterDTO;
import com.gl.project.entities.UserGroups;
import com.gl.project.repository.UserRepository;
import io.cucumber.java.pt.*;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserStepDefinition {

    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private UserRepository userRepository;

    public UserStepDefinition(TestRestTemplate restTemplate, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
    }

    private ResponseEntity<?> response;

    @Dado("que o sistema não possui um usuário com o email {string}")
    public void que_o_sistema_nao_possui_um_usuario_com_o_email(String email) {
        assertNull(userRepository.findByEmail(email));
    }

    @Quando("eu registro um novo usuário com os seguintes dados:")
    public void eu_registro_um_novo_usuario_com_os_seguintes_dados(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);

        RegisterDTO dto = new RegisterDTO(
                data.get("name"),
                data.get("email"),
                data.get("password"),
                UserGroups.valueOf(data.get("groups")),
                data.get("cpf")
        );

        String url = "http://localhost:" + port + "/auth/register";
        response = restTemplate.postForEntity(url, dto, Response.class);
    }

    @Entao("o sistema deve retornar status {int}")
    public void o_sistema_deve_retornar_status(Integer statusCode) {
        assertEquals(statusCode, response.getStatusCodeValue());
    }
}