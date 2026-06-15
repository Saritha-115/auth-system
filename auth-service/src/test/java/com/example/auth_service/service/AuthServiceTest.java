package com.example.auth_service.service;

import com.example.auth_service.dto.AuthDtos.LoginRequest;
import com.example.auth_service.dto.AuthDtos.LoginResponse;
import com.example.auth_service.model.User;
import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setRole("ROLE_USER");
        testUser.setEnabled(true);

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");
    }

    @Test
    void login_WithValidCredentials_ReturnsToken() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("testuser", "ROLE_USER")).thenReturn("generated.jwt.token");

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("generated.jwt.token", response.getToken());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void login_WithInvalidUser_ThrowsException() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_WithWrongPassword_ThrowsException() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);
        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }
}