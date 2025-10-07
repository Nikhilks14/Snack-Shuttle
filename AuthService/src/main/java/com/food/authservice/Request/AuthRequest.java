package com.food.authservice.Request;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}