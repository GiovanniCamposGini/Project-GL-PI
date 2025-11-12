package com.gl.project.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_users")
public class User implements Serializable, UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CPF
    private String CPF;

    @NotBlank
    @Size(min = 3, message = "Nome não pode ter menos que 3 caracteres")
    private String name;

    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private UserGroups groups;

    @NotBlank(message = "Status não pode ser nulo")
    private String statusBanco;

    @Size(min = 6, message = "Senha tem que ter no mínimo 6 dígitos")
    private String password;

    private String cep;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    public User() {
    }

    public User(String name, String email, String password, UserGroups groups, String cpf) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.groups = groups;
        this.CPF = cpf;
        this.statusBanco = "ATIVO";
    }

    public User(long id, String name, String email, String CPF, String password, UserGroups groups) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.CPF = CPF;
        this.statusBanco = "active";
        this.password = password;
        this.groups = groups;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserGroups getGroups() {
        return groups;
    }

    public void setGroups(UserGroups groups) {
        this.groups = groups;
    }

    public String getStatusBanco() {
        return statusBanco;
    }

    public void setStatusBanco(String statusBanco) {
        this.statusBanco = statusBanco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public List<Order> getOrders() {
        return orders;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.groups == UserGroups.ADMIN) {
            return List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // equals e hashCode
    @Override
    public int hashCode() {
        return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User other = (User) obj;
        return Objects.equals(id, other.id);
    }
}
