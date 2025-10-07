package com.foodDelivery.UserService.services;

import com.foodDelivery.UserService.dto.UpdateUserDto;
import com.foodDelivery.UserService.entity.AppUser;
import com.foodDelivery.UserService.model.Role;
import com.foodDelivery.UserService.repo.UserRepository;
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
}