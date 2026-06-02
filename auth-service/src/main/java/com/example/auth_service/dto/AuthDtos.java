package com.example.auth_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class AuthDtos {

    @Data
    public static class LoginRequest {
        @NotBlank(message = "Username is required")
        private String username;

        @NotBlank(message = "Password is required")
        private String password;
    }

    @Data
    public static class LoginResponse {
        private String token;
        private String tokenType = "Bearer";
        private Long userId;
        private String username;
        private String email;
        private String role;

        public LoginResponse(String token, Long userId, String username, String email, String role) {
            this.token = token;
            this.userId = userId;
            this.username = username;
            this.email = email;
            this.role = role;
        }
    }

    @Data
    public static class ValidateRequest {
        @NotBlank
        private String token;
    }

    @Data
    public static class ValidateResponse {
        private boolean valid;
        private String username;
        private String role;

        public ValidateResponse(boolean valid, String username, String role) {
            this.valid = valid;
            this.username = username;
            this.role = role;
        }
    }

    @Data
    public static class ErrorResponse {
        private String message;
        private int status;

        public ErrorResponse(String message, int status) {
            this.message = message;
            this.status = status;
        }
    }
}