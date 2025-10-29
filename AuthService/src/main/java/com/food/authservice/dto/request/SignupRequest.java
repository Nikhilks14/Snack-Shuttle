package com.food.authservice.dto.request;

import com.food.authservice.entity.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private Role role;
}