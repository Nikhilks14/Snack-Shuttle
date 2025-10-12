package com.food.authservice.controller;

import com.food.authservice.AuthService;
import com.food.authservice.Request.AuthRequest;
import com.food.authservice.Request.SignupRequest;
import com.food.authservice.client.UserClient;
import com.food.authservice.response.AuthResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserClient userClient;

    @GetMapping("/checkUser/{email}")
    public ResponseEntity<String> checkUser(@PathVariable String email){
        String userResponse = userClient.getUserByEmail(email);
        return ResponseEntity.ok("Auth Service -> " + userResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request){
        return ResponseEntity.ok(authService.signUp(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestParam("refreshToken") String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestParam("email") String email,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        authService.logout(email,token);
        return ResponseEntity.ok("Logged out and all tokens revoked.");
    }
}
