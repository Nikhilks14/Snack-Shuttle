package com.food.OrderService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleChangeRequest {
    private String role; // e.g. "ROLE_ADMIN"
}