package com.food.OrderService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String username;
    private List<String> roles;
}