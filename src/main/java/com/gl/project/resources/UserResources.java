package com.gl.project.resources;


import com.gl.project.entities.User;
import com.gl.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping(value = "/users")

public class UserResources {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> list = userService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        User user = userService.findByID(id);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody User newUser) {
        User user = userService.create(newUser);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User newUser) {
        User user = userService.update(id, newUser);
        return ResponseEntity.ok().body(user);
    }
    @PutMapping(value = "/{id}/status")
    public ResponseEntity<User> updateStatus(@PathVariable Long id, @RequestBody User newUser) {
        User user = userService.updateStatus(id, newUser);
        return ResponseEntity.ok().body(user);
    }
}
