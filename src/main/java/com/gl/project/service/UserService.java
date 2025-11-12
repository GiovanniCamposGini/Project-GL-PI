package com.gl.project.service;

import com.gl.project.VCR.entities.ViaCEPResponse;
import com.gl.project.VCR.service.VCRService;
import com.gl.project.VCR.service.ViaCepService;
import com.gl.project.entities.User;
import com.gl.project.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ViaCepService viaCepService;

    @Autowired
    private VCRService vcrService;

    @Autowired
    private Validator validator;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByID(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));
    }

    public User create(User user) {
        var violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return userRepository.save(user);
    }

    public User update(Long id, User newUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));

        String encryptedPassword = new BCryptPasswordEncoder().encode(newUser.getPassword());
        user.setPassword(encryptedPassword);

        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        user.setGroups(newUser.getGroups());

        return userRepository.save(user);
    }

    public User updateStatus(Long id, User newUser) {
        User userStatus = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + id));
        userStatus.setStatusBanco(newUser.getStatusBanco());
        return userRepository.save(userStatus);
    }

    public ViaCEPResponse buscarEnderecoDoUser(Long id, boolean recordMode) {
        return userRepository.findById(id)
                .map(user -> {
                    String cassetteName = "cep_" + user.getCep();
                    try {
                        if (recordMode) {
                            ViaCEPResponse response = viaCepService.buscarEnderecoPorCEP(user.getCep());
                            vcrService.save(cassetteName, response);
                            return response;
                        } else {
                            ViaCEPResponse response = vcrService.load(cassetteName, ViaCEPResponse.class);
                            return response;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Erro ao buscar endereço", e);
                    }
                })
                .orElse(null);
    }
}
