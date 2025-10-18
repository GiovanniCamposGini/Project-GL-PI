package com.gl.project.entities;

public record RegisterDTO(String name, String email, String password, UserGroups groups, String cpf) {
}
