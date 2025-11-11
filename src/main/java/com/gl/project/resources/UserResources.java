package com.gl.project.resources;


<<<<<<< HEAD
=======
import com.gl.project.VCR.entities.ViaCEPResponse;
>>>>>>> 264a16fbf826ee630aa2bbc602e7497b44616f1d
import com.gl.project.entities.DTO.OrderResponseDTO;
import com.gl.project.entities.User;
import com.gl.project.entities.DTO.UserResponseDTO;
import com.gl.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import java.util.List;

@RestController
@RequestMapping(value = "/users")

public class UserResources {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        List<User> list = userService.findAll();
        List<UserResponseDTO> userDTO = list.stream()
                .map(UserResponseDTO::new)
                .toList();
        return ResponseEntity.ok().body(userDTO);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        User user = userService.findByID(id);
        UserResponseDTO dto = new UserResponseDTO(user);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody User newUser) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(newUser.getPassword());
        newUser.setPassword(encryptedPassword);
        User user = userService.create(newUser);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User newUser) {
        User user = userService.update(id, newUser);
        return ResponseEntity.ok().body(user);
    }
<<<<<<< HEAD
=======

>>>>>>> 264a16fbf826ee630aa2bbc602e7497b44616f1d
    @PutMapping(value = "/{id}/status")
    public ResponseEntity<User> updateStatus(@PathVariable Long id, @RequestBody User newUser) {
        User user = userService.updateStatus(id, newUser);
        return ResponseEntity.ok().body(user);
    }
<<<<<<< HEAD
=======

    @GetMapping("/{id}/endereco")
    public ResponseEntity<ViaCEPResponse> getEndereco(@PathVariable Long id, @RequestParam(name = "recordmode", defaultValue = "false") boolean recordMode) {
        ViaCEPResponse endereco = userService.buscarEnderecoDoUser(id, recordMode);
        if (endereco != null) {
            return ResponseEntity.ok(endereco);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

>>>>>>> 264a16fbf826ee630aa2bbc602e7497b44616f1d
}
