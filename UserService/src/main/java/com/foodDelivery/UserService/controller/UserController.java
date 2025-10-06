package com.foodDelivery.UserService.controller;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    // returns logged-in user details (token must be valid)
    @GetMapping("/me")
    public ResponseEntity<UserDto> me(org.springframework.security.core.Authentication authentication) {
// authentication.getName() returns subject (email) set by JwtAuthFilter
        String email = authentication.getName();
        AppUser u = userService.getByEmail(email);
        UserDto dto = new UserDto(u.getId(), u.getEmail(), u.getName(), u.getPhone(), u.getRole());
        return ResponseEntity.ok(dto);
    }
    @PutMapping("/me")
    public ResponseEntity<UserDto> updateMe(org.springframework.security.core.Authentication authentication,
                                            @RequestBody UpdateUserDto dto) {
        String email = authentication.getName();
        AppUser u = userService.getByEmail(email);
        AppUser updated = userService.updateProfile(u.getId(), dto);
        UserDto out = new UserDto(updated.getId(), updated.getEmail(), updated.getName(), updated.getPhone(), updated.getRole());
        return ResponseEntity.ok(out);
    }
}