package com.food.authservice.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {

    @Email(message = "Invalid email fromat")
    @NotBlank(message =  "Email is required")
    private String email;

    @NotBlank(message =  "Password is required")
    private String password;
}