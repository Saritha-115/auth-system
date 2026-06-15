package com.example.user_service.service;

import com.example.user_service.dto.UserDtos.RegisterRequest;
import com.example.user_service.dto.UserDtos.UserResponse;
import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterRequest registerRequest;
    private User savedUser;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setEmail("new@example.com");
        registerRequest.setPassword("SecurePass123");
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");

        savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("newuser");
        savedUser.setEmail("new@example.com");
        savedUser.setPassword("encodedPassword");
        savedUser.setFirstName("John");
        savedUser.setLastName("Doe");
        savedUser.setRole("ROLE_USER");
        savedUser.setEnabled(true);
    }

    @Test
    void register_WithValidData_ReturnsUserResponse() {
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("SecurePass123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse result = userService.register(registerRequest);

        assertNotNull(result);
        assertEquals("newuser", result.getUsername());
        assertEquals("new@example.com", result.getEmail());

        verify(userRepository).existsByUsername("newuser");
        verify(userRepository).existsByEmail("new@example.com");
        verify(passwordEncoder).encode("SecurePass123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_WithDuplicateUsername_ThrowsException() {
        when(userRepository.existsByUsername("newuser")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> userService.register(registerRequest));
    }

    @Test
    void getUserById_WhenUserExists_ReturnsUserResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        UserResponse result = userService.getUserById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ThrowsException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(99L));
    }
}