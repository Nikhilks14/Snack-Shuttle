package com.food.authservice.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String username;
    private String email;
    private String role;
}