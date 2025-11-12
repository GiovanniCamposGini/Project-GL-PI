package com.gl.project.user;

import com.gl.project.entities.User;
import com.gl.project.entities.UserGroups;
import com.gl.project.repository.UserRepository;
import com.gl.project.service.UserService;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private Validator validator;

    @Test
    void deveRetornarListaDeUsuarios() {

        User user1 = new User("Pedro", "pedro@email.com", "123", UserGroups.USER, "12345678900");
        User user2 = new User("Lenas", "lenas@email.com", "456", UserGroups.ADMIN, "98765432100");
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));


        List<User> result = userService.findAll();


        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll();
    }


    @Test
    void deveRetornarUsuarioPorId() {

        User user = new User(1L, "Pedro", "pedro@email.com", "123", "123", UserGroups.USER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));


        User result = userService.findByID(1L);


        assertNotNull(result);
        assertEquals("Pedro", result.getName());
        verify(userRepository).findById(1L);
    }


    @Test
    void deveCriarUsuario() {
        User user = new User("João", "joao@email.com", "senha", UserGroups.USER, "11122233344");

        when(validator.validate(any(User.class))).thenReturn(Collections.emptySet());
        when(userRepository.save(user)).thenReturn(user);

        User created = userService.create(user);

        assertEquals("João", created.getName());
        verify(userRepository, times(1)).save(user);
    }


    @Test
    void deveAtualizarUsuario() {

        User userAntigo = new User(1L, "Pedro", "pedro@email.com", "123", "senhaAntiga", UserGroups.USER);
        User userNovo = new User(1L, "Pedro Atualizado", "novo@email.com", "123", "novaSenha", UserGroups.ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userAntigo));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0)); // retorna o próprio objeto salvo


        User atualizado = userService.update(1L, userNovo);


        assertEquals("Pedro Atualizado", atualizado.getName());
        assertEquals("novo@email.com", atualizado.getEmail());
        assertEquals(UserGroups.ADMIN, atualizado.getGroups());


        assertNotEquals("novaSenha", atualizado.getPassword());

        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }


    @Test
    void deveAtualizarStatusDoUsuario() {

        User user = new User(1L, "Pedro", "pedro@email.com", "123", "senha", UserGroups.USER);
        user.setStatusBanco("ATIVO");

        User novoStatus = new User();
        novoStatus.setStatusBanco("INATIVO");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));


        User atualizado = userService.updateStatus(1L, novoStatus);


        assertEquals("INATIVO", atualizado.getStatusBanco());
        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
    }
}
