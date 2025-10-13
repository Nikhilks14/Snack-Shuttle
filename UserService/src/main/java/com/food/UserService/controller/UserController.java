package com.food.UserService.controller;

import com.food.UserService.dto.UpdateUserDto;
import com.food.UserService.dto.UserDto;
import com.food.UserService.entity.AppUser;
import com.food.UserService.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping()
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
        AppUser created = userService.createUser(userDto);
        UserDto response = new UserDto(
                created.getId(),
                created.getEmail(),
                created.getName(),
                created.getPhone(),
                created.getRole()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        AppUser u = userService.getByEmail(email);
        UserDto dto = new UserDto(u.getId(), u.getEmail(), u.getName(), u.getPhone(), u.getRole());
        return ResponseEntity.ok(dto);
    }


    // returns logged-in user details (token must be valid)
    @GetMapping("/me")
    public ResponseEntity<UserDto> me(Authentication authentication) {
// authentication.getName() returns subject (email) set by JwtAuthFilter
        String email = authentication.getName();
        AppUser u = userService.getByEmail(email);
        UserDto dto = new UserDto(u.getId(), u.getEmail(), u.getName(), u.getPhone(), u.getRole());
        return ResponseEntity.ok(dto);
    }
    @PutMapping("/me")
    public ResponseEntity<UserDto> updateMe(Authentication authentication,
                                            @RequestBody UpdateUserDto dto) {
        String email = authentication.getName();
        AppUser u = userService.getByEmail(email);
        AppUser updated = userService.updateProfile(u.getId(), dto);
        UserDto out = new UserDto(updated.getId(), updated.getEmail(), updated.getName(), updated.getPhone(), updated.getRole());
        return ResponseEntity.ok(out);
    }
}