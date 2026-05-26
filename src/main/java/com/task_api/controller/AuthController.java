package com.task_api.controller;

import com.task_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Autenticação e cadastro de usuários")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Cadastrar usuário")
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @RequestBody @Valid RegisterRequest request) {

        String token = authService.register(
                request.name(), request.email(), request.password()
        );

        return ResponseEntity.ok(Map.of("token", token));
    }

    @Operation(summary = "Login")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody @Valid LoginRequest request) {

        String token = authService.login(request.email(), request.password());
        return ResponseEntity.ok(Map.of("token", token));
    }

    public record RegisterRequest(
            @NotBlank(message = "O nome não pode ser vazio.") String name,
            @NotBlank @Email(message = "Email inválido.") String email,
            @NotBlank @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.") String password
    ) {}

    public record LoginRequest(
            @NotBlank @Email(message = "Email inválido.") String email,
            @NotBlank(message = "A senha não pode ser vazia.") String password
    ) {}
}