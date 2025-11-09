package com.gl.project.user;

import com.gl.project.entities.User;
import com.gl.project.entities.UserGroups;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void UserRoleAdmin() {
        // Arrange
        User user = new User("Pedro", "pedro@email.com", "123", UserGroups.ADMIN, "12345678900");

        // Act
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // Assert
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertEquals(2, authorities.size());
    }

    @Test
    void UserRoleUser() {
        User user = new User("João", "joao@email.com", "123", UserGroups.USER, "11122233344");

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertEquals(1, authorities.size());
    }

    @Test
    void EmailComoUsername() {
        User user = new User("Maria", "maria@email.com", "123", UserGroups.USER, "22233344455");

        assertEquals("maria@email.com", user.getUsername());
    }

    @Test
    void metodosDeStatusDevemRetornarTrue() {
        User user = new User("Ana", "ana@email.com", "123", UserGroups.USER, "33344455566");

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    void equalsDeveCompararPorId() {
        User user1 = new User(1L, "Pedro", "pedro@email.com", "123", "123", UserGroups.USER);
        User user2 = new User(1L, "Pedro", "pedro@email.com", "123", "123", UserGroups.USER);
        User user3 = new User(2L, "João", "joao@email.com", "123", "123", UserGroups.USER);

        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
    }
}
