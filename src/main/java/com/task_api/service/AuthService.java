package com.task_api.service;

import com.task_api.model.User;
import com.task_api.repository.UserRepository;
import com.task_api.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public String register(String name, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email já cadastrado.");
        }

        User user = new User(name, email, passwordEncoder.encode(password));
        userRepository.save(user);
        return jwtService.generateToken(user);
    }

    public String login(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        return jwtService.generateToken(user);
    }
}