package com.gl.project.service;

import com.gl.project.entities.User;
import com.gl.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByID(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.get();
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public User update(Long id, User newUser) {
        User user = userRepository.findById(id).get();
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        user.setPassword(newUser.getPassword());
        user.setGroup(newUser.getGroup());
        return userRepository.save(user);
    }

    public User updateStatus(Long id, User newUser) {
        User userStatus = userRepository.findById(id).get();
        userStatus.setStatusBanco(newUser.getStatusBanco());
        return userRepository.save(userStatus);
    }
}
