package com.food.authservice.service;

import com.food.authservice.dto.request.AuthRequest;
import com.food.authservice.dto.request.SignupRequest;
import com.food.authservice.dto.request.UserRequest;
import com.food.authservice.dto.response.AuthResponse;
import com.food.authservice.entity.RefreshToken;
import com.food.authservice.entity.Role;
import com.food.authservice.entity.User;
import com.food.authservice.repo.RefreshTokenRepository;
import com.food.authservice.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthResponse signUp(SignupRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already register");
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : Role.CUSTOMER)
                .build();

        userRepository.save(user);

        UserRequest userRequest = new UserRequest(
                request.getUsername(),
                request.getEmail(),
                request.getRole() != null ? request.getRole().name() : "CUSTOMER"
        );


        return issueToken(user);
    }

    public AuthResponse login(AuthRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Revoke old token
        refreshTokenRepository.revokeAllTokenByUser(user);

        // Issue new tokens
        return issueToken(user);
    }

    public AuthResponse refreshToken(String oldrefreshToken) {

        RefreshToken storedToken = refreshTokenRepository.findByToken(oldrefreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if(storedToken.isRevoked() || storedToken.getExpiryDate().isBefore(Instant.now()) ){
            throw new RuntimeException("Refresh token has been revoked");
        }

        User user = storedToken.getUser();


        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);


        // Rotate Token : revoke old , issue new
        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        jwtService.blacklistToken(oldrefreshToken);
        return issueToken(user);

    }

    public void logout(String email, String accessToken){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user not fount"));

        refreshTokenRepository.revokeAllTokenByUser(user);
        // block this access token immediately
        jwtService.blacklistToken(accessToken);
    }


    // -------------HELPER --------------- //

    @Transactional
    private AuthResponse issueToken(User user){

        // Revoke all old token
        refreshTokenRepository.revokeAllTokenByUser(user);

        // Step 2 : Create new token
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken =jwtService.generateRefreshToken(user);

        RefreshToken tokenEntity = RefreshToken.builder()
                .token(refreshToken)
                .expiryDate(Instant.now().plus(1, ChronoUnit.DAYS))
                .isRevoked(false)
                .user(user)
                .build();


        // If you ever want to allow reusing the same token string (for debugging or rotation safety):
        // refreshTokenRepository.findByToken(refreshToken).ifPresent(refreshTokenRepository::delete);

        // Step 3 Save new token After revocation
        refreshTokenRepository.save(tokenEntity);

        return new AuthResponse(accessToken, refreshToken);
    }

//    private String createAndsaveRefreshToken (User user){
//        String token = jwtService.generateRefreshToken(user);
//
//        RefreshToken refreshToken = RefreshToken.builder()
//                .token(token)
//                .user(user)
//                .expiryDate(Instant.now().plusSeconds(24*60*60))
//                .isRevoked(false
//                )
//                .build();
//
//        RefreshToken save = refreshTokenRepository.save(refreshToken);
//
//        System.out.printf("Saved refresh token for user " + user.getEmail() );
//        return  token;
//    }

}