package com.food.authservice.service;

import com.food.authservice.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();
    String deleteUser(Long id);
    UserDTO getCurrentUserProfile();
    String updateProfile(UserDTO dto);
    public String toggleUserActive(Long id, boolean active);
}
