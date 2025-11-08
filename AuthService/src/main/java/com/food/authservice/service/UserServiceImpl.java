package com.food.authservice.service;

import com.food.authservice.dto.UserDTO;
import com.food.authservice.entity.Role;
import com.food.authservice.entity.User;
import com.food.authservice.repo.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    @Override
    public String deleteUser(Long id) {
        if(!userRepository.existsById(id)){
            throw new RuntimeException("User not fount");
        }
        userRepository.deleteById(id);
        return "User delete successfully";
    }

    @Override
    public UserDTO getCurrentUserProfile() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return convertToDTO(user);
    }

    @Override
    public String updateProfile(UserDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(dto.getUsername() != null ? dto.getUsername() : user.getUsername());
        user.setEmail(dto.getEmail() != null ? dto.getEmail() : user.getEmail());
        userRepository.save(user);

        return "Profile updated successfully!";
    }

    @Override
    public String toggleUserActive(Long id, boolean active) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(active);
        userRepository.save(user);
        return active ? "User activated" : "User deactivated";
    }


    public String updateUserRole(Long id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(role);
        userRepository.save(user);
        return "User role updated to " + role.name();
    }


    private UserDTO convertToDTO(User user) {

        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .build();
    }

}
