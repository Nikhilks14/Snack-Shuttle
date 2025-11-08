package com.food.authservice.controller;

import com.food.authservice.dto.UserDTO;
import com.food.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<String> activateUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.toggleUserActive(id, true));
    }
//
//    @PutMapping("/deactivate/{id}")
//    public ResponseEntity<String> deactivateUser(@PathVariable Long id) {
//        return ResponseEntity.ok(userService.toggleUserActive(id, false));
//    }
//
//    @PutMapping("/role/{id}")
//    public ResponseEntity<String> updateRole(@PathVariable Long id, @RequestParam Role role) {
//        return ResponseEntity.ok(userService.updateUserRole(id, role));
//    }

}
