package com.example.auth_service.controller;

import com.example.auth_service.dto.AuthDtos;
import com.example.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDtos.LoginRequest request) {
        try {
            AuthDtos.LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401)
                    .body(new AuthDtos.ErrorResponse(e.getMessage(), 401));
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<AuthDtos.ValidateResponse> validate(
            @RequestBody AuthDtos.ValidateRequest request) {
        AuthDtos.ValidateResponse response = authService.validateToken(request.getToken());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Auth Service is running");
    }
}

