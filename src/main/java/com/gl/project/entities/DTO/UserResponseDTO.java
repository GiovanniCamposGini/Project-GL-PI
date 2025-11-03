package com.gl.project.entities.DTO;

import com.gl.project.entities.User;

public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String groups;
    private String statusBanco;
    private String cpf;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.groups = user.getGroups().toString();
        this.statusBanco = user.getStatusBanco();
        this.cpf = user.getCPF();
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getGroups() { return groups; }
    public String getStatusBanco() { return statusBanco; }
    public String getCpf() { return cpf; }
}
