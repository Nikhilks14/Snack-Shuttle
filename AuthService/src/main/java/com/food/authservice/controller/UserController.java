package com.food.authservice.controller;

import com.food.authservice.dto.UserDTO;
import com.food.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getProfile() {
        return ResponseEntity.ok(userService.getCurrentUserProfile());
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateProfile(@RequestBody UserDTO dto) {
        return ResponseEntity.ok(userService.updateProfile(dto));
    }
}