package com.gl.project.service;

import com.gl.project.VCR.entities.ViaCEPResponse;
import com.gl.project.VCR.service.VCRService;
import com.gl.project.VCR.service.ViaCepService;
import com.gl.project.entities.User;
import com.gl.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ViaCepService viaCepService;
    @Autowired
    private final VCRService vcrService;

    public UserService(VCRService vcrService) {
        this.vcrService = vcrService;
    }

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

        String encryptedPassword = new BCryptPasswordEncoder().encode(newUser.getPassword());
        user.setPassword(encryptedPassword);

        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        user.setGroups(newUser.getGroups());
        return userRepository.save(user);
    }

    public User updateStatus(Long id, User newUser) {
        User userStatus = userRepository.findById(id).get();
        userStatus.setStatusBanco(newUser.getStatusBanco());
        return userRepository.save(userStatus);
    }

    public ViaCEPResponse buscarEnderecoDoUser(Long id, boolean recordMode) throws RuntimeException {
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
                            if (response != null) {
                                return response;
                            }
                            return null;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("Erro ao buscar endereço", e);
                    }
                })
                .orElse(null);
    }
}
