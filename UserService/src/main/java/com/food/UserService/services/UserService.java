package com.food.UserService.services;

import com.food.UserService.dto.UpdateUserDto;
import com.food.UserService.dto.UserDto;
import com.food.UserService.entity.AppUser;
import com.food.UserService.model.Role;
import com.food.UserService.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public AppUser getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }


    public AppUser getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }


    public List<AppUser> listAll() {
        return userRepository.findAll();
    }

    public AppUser updateProfile(Long userId, UpdateUserDto dto) {
        AppUser u = getById(userId);
        if (dto.getName() != null) u.setName(dto.getName());
        if (dto.getPhone() != null) u.setPhone(dto.getPhone());
        return userRepository.save(u);
    }


    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }


    // Admin only
    public AppUser changeRole(Long userId, Role role) {
        AppUser u = getById(userId);
        u.setRole(role);
        return userRepository.save(u);
    }

    public AppUser createUser(UserDto  dto) {
        AppUser user = new AppUser();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setRole(dto.getRole());
        return userRepository.save(user);
    }
}