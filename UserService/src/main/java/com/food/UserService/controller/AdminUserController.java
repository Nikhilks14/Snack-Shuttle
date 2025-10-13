package com.food.UserService.controller;


import com.food.UserService.dto.UserDto;
import com.food.UserService.entity.AppUser;
import com.food.UserService.model.Role;
import com.food.UserService.services.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
    private final UserService userService;


    @GetMapping
    public ResponseEntity<List<UserDto>> listAll() {
        List<UserDto> list = userService.listAll().stream()
                .map(u -> new UserDto(u.getId(), u.getEmail(), u.getName(), u.getPhone(), u.getRole()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }


    @PostMapping("/{id}/role")
    public ResponseEntity<UserDto> changeRole(@PathVariable Long id, @RequestBody Role newRole) {
        AppUser updated = userService.changeRole(id, newRole);
        UserDto out = new UserDto(updated.getId(), updated.getEmail(), updated.getName(), updated.getPhone(), updated.getRole());
        return ResponseEntity.ok(out);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}