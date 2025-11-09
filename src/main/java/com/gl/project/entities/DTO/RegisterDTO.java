package com.gl.project.entities.DTO;

import com.gl.project.entities.UserGroups;

public record RegisterDTO(String name, String email, String password, UserGroups groups, String cpf) {
}
