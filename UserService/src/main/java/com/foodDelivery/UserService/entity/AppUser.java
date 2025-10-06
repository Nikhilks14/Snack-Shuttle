package com.foodDelivery.UserService.entity;

import com.foodDelivery.UserService.model.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true, nullable = false)
    private String email;


    private String name;


    private String phone;


    @Enumerated(EnumType.STRING)
    private Role role;
}