package com.example.user_service.service;

import com.example.user_service.dto.UserDtos;
import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDtos.UserResponse register(UserDtos.RegisterRequest request) {

        log.info("Checking if user exists: username={}, email={}",
                request.getUsername(), request.getEmail());

        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("USERNAME EXISTS: {}", request.getUsername());
            throw new IllegalArgumentException("Username already taken");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("EMAIL EXISTS: {}", request.getEmail());
            throw new IllegalArgumentException("Email already registered");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        User saved = userRepository.save(user);

        log.info("USER SAVED with id={}", saved.getId());

        return new UserDtos.UserResponse(saved);
    }

    public UserDtos.UserResponse getUserById(Long id) {
        log.info("Fetching user id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("USER NOT FOUND id={}", id);
                    return new IllegalArgumentException("User not found with id: " + id);
                });

        return new UserDtos.UserResponse(user);
    }

    @Transactional
    public UserDtos.UserResponse updateUser(Long id, UserDtos.UpdateRequest request) {

        log.info("Updating user id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());

        if (request.getEmail() != null &&
                !request.getEmail().equals(user.getEmail())) {

            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already in use");
            }

            user.setEmail(request.getEmail());
        }

        User updated = userRepository.save(user);

        log.info("USER UPDATED id={}", updated.getId());

        return new UserDtos.UserResponse(updated);
    }

    @Transactional
    public void deleteUser(Long id) {

        log.info("Deleting user id={}", id);

        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }

        userRepository.deleteById(id);

        log.info("USER DELETED id={}", id);
    }
}