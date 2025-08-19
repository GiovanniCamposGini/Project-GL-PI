package com.gl.project.resources;

import com.gl.project.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")

public class UserResources {

    @GetMapping

    public ResponseEntity<User> findAll() {
        User user = new User(1L, "Maria", "maria@gmail.com", "999999999", "12345");
        return ResponseEntity.ok(user);

    }
}
