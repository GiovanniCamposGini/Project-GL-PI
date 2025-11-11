package com.gl.project.entities.DTO;

import com.gl.project.entities.User;
<<<<<<< HEAD

public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String groups;
    private String statusBanco;
=======
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserResponseDTO {
    private Long id;

    @NotBlank(message = "Nome não pode estar em branco")
    @Size(min = 3, message = "Nome deve ter no mínimo 3 caracteres")
    private String name;

    @NotBlank(message = "Email não pode estar em branco")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Grupo não pode estar em branco")
    private String groups;

    @NotBlank(message = "Status do banco não pode estar em branco")
    private String statusBanco;

    @NotBlank(message = "CPF não pode estar em branco")
    @Size(min = 11, max = 11, message = "CPF deve ter exatamente 11 caracteres")
>>>>>>> 264a16fbf826ee630aa2bbc602e7497b44616f1d
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
