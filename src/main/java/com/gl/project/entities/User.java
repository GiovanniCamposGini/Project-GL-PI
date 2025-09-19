package com.gl.project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_users")
public class User implements Serializable, UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.groups == UserGroups.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
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

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CPF
    private String CPF;
    private String name;

    @Email
    private String email;
    private UserGroups groups;
    private String statusBanco = "Active";
    private String password;

    public User(){
    }

    public User(long id, String name, String email, String CPF, String password, UserGroups groups) {
        super();
        this.id = id;
        this.name = name;
        this.email = email;
        this.CPF = CPF;
        this.statusBanco = "active";
        this.password = password;
        this.groups = groups;
    }

    public long getId() {
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

    public void setCPF(String CPF) {this.CPF = CPF; }

    public String getPassword() {
        return password;
    }

    public UserGroups getGroup() {
        return groups;
    }

    public void setGroup(UserGroups group) {
        this.groups = group;
    }

    public String getStatusBanco() {
        return statusBanco;
    }

    public void setStatusBanco(String statusBanco) {
        this.statusBanco = statusBanco;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
