package com.gl.project.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gl.project.entities.User;
import com.gl.project.entities.UserGroups;
import com.gl.project.resources.UserResources;
import com.gl.project.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserResourcesTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserResources userResources;

    private User user;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userResources).build();


        user = new User(1L, "Pedro", "pedro@email.com", "12345678900", "senha123", UserGroups.USER);
    }

    @Test
    void deveRetornarListaDeUsuarios() throws Exception {
        when(userService.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pedro"))
                .andExpect(jsonPath("$[0].email").value("pedro@email.com"));

        verify(userService, times(1)).findAll();
    }

    @Test
    void deveRetornarUsuarioPorId() throws Exception {
        when(userService.findByID(1L)).thenReturn(user);

        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pedro"))
                .andExpect(jsonPath("$.email").value("pedro@email.com"));

        verify(userService, times(1)).findByID(1L);
    }

    @Test
    void deveCriarNovoUsuario() throws Exception {
        when(userService.create(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Pedro"))
                .andExpect(jsonPath("$.email").value("pedro@email.com"));

        verify(userService, times(1)).create(any(User.class));
    }

    @Test
    void deveAtualizarUsuario() throws Exception {
        User newUser = new User(1L, "Pedro Atualizado", "novo@email.com", "12345678900", "novaSenha", UserGroups.ADMIN);
        when(userService.update(eq(1L), any(User.class))).thenReturn(newUser);

        mockMvc.perform(put("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pedro Atualizado"))
                .andExpect(jsonPath("$.email").value("novo@email.com"));

        verify(userService, times(1)).update(eq(1L), any(User.class));
    }

    @Test
    void deveAtualizarStatusDoUsuario() throws Exception {
        User statusAtualizado = new User(1L, "Pedro", "pedro@email.com", "12345678900", "senha123", UserGroups.USER);
        statusAtualizado.setStatusBanco("INATIVO");

        when(userService.updateStatus(eq(1L), any(User.class))).thenReturn(statusAtualizado);

        mockMvc.perform(put("/users/{id}/status", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusAtualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusBanco").value("INATIVO"));

        verify(userService, times(1)).updateStatus(eq(1L), any(User.class));
    }
}
