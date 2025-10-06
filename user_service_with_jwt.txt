# Project: user-service (Spring Boot microservice)

This document contains a ready-to-use **User Service** (no login) and the **JWT validation snippets** other microservices should use to validate incoming access tokens issued by the Auth Service. The Auth Service is assumed to issue JWTs with these claims:
- `sub` (email)
- `userId` (numeric id)
- `role` (CUSTOMER, ADMIN, RESTAURANT, DELIVERY_BOY)

---

## 1) pom.xml (important dependencies)

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi=...>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>user-service</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <packaging>jar</packaging>
  <name>user-service</name>

  <properties>
    <java.version>17</java.version>
    <spring.boot.version>3.1.4</spring.boot.version>
  </properties>

  <dependencies>
    <!-- Spring Boot starters -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- Database (H2 for quick testing) -->
    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>runtime</scope>
    </dependency>

    <!-- JWT parsing -->
    <dependency>
      <groupId>io.jsonwebtoken</groupId>
      <artifactId>jjwt-api</artifactId>
      <version>0.11.5</version>
    </dependency>
    <dependency>
      <groupId>io.jsonwebtoken</groupId>
      <artifactId>jjwt-impl</artifactId>
      <version>0.11.5</version>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>io.jsonwebtoken</groupId>
      <artifactId>jjwt-jackson</artifactId>
      <version>0.11.5</version>
      <scope>runtime</scope>
    </dependency>

    <!-- Lombok -->
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <optional>true</optional>
    </dependency>

    <!-- Validation -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>
</project>
```

---

## 2) application.yml (or application.properties)

```yaml
server:
  port: 8081
spring:
  datasource:
    url: jdbc:h2:mem:userdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

# JWT config - THIS SECRET MUST MATCH AUTH SERVICE (or use public key if asymmetric)
security:
  jwt:
    secret: "replace_with_the_same_base64_secret_used_by_auth_service"
```

---

## 3) Project structure (important files)

```
src/main/java/com/example/userservice
  ├─ UserServiceApplication.java
  ├─ config
  │   ├─ SecurityConfig.java
  │   └─ JwtAuthFilter.java
  ├─ controller
  │   ├─ UserController.java
  │   └─ AdminUserController.java
  ├─ dto
  │   ├─ UserDto.java
  │   ├─ UpdateUserDto.java
  │   └─ ChangePasswordDto.java
  ├─ model
  │   ├─ AppUser.java
  │   └─ Role.java
  ├─ repository
  │   └─ UserRepository.java
  └─ service
      └─ UserService.java
```

---

## 4) Main application

```java
package com.example.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
```

---

## 5) Model & DTOs

```java
// model/Role.java
package com.example.userservice.model;

public enum Role {
    CUSTOMER,
    ADMIN,
    RESTAURANT,
    DELIVERY_BOY
}

// model/AppUser.java
package com.example.userservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String name;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;
}

// dto/UserDto.java
package com.example.userservice.dto;

import com.example.userservice.model.Role;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private Role role;
}

// dto/UpdateUserDto.java
package com.example.userservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    private String name;
    private String phone;
}

// dto/ChangePasswordDto.java
package com.example.userservice.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {
    private String oldPassword;
    private String newPassword;
}
```

---

## 6) Repository

```java
package com.example.userservice.repository;

import com.example.userservice.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
}
```

---

## 7) Service

```java
package com.example.userservice.service;

import com.example.userservice.dto.UpdateUserDto;
import com.example.userservice.model.AppUser;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public AppUser changeRole(Long userId, com.example.userservice.model.Role role) {
        AppUser u = getById(userId);
        u.setRole(role);
        return userRepository.save(u);
    }
}
```

---

## 8) Controllers

```java
package com.example.userservice.controller;

import com.example.userservice.dto.UpdateUserDto;
import com.example.userservice.dto.UserDto;
import com.example.userservice.model.AppUser;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

// Admin controller - restricted to ADMIN role
package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.model.AppUser;
import com.example.userservice.model.Role;
import com.example.userservice.service.UserService;
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
```

---

## 9) Security: JwtAuthFilter & SecurityConfig

> Important: the filter decodes and validates the JWT issued by Auth Service. It sets a `UsernamePasswordAuthenticationToken` with `principal=email` and authority `ROLE_<role>`.

```java
// config/JwtAuthFilter.java
package com.example.userservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Value("${security.jwt.secret}")
    private String secret;

    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = header.substring(7);
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(email, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception ex) {
            // token invalid/expired
            // optional: you can send 401 here, but prefer letting Spring handle unauthenticated access
        }

        filterChain.doFilter(request, response);
    }
}

// config/SecurityConfig.java
package com.example.userservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**", "/error").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/users/me").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```

Notes:
- The secret must be exactly the same as used by your Auth Service (or use public key + RSA verification if using asymmetric JWTs).
- If token verification fails, the request will continue unauthenticated, and Spring will return 401 for endpoints that require authentication.

---

## 10) How other services (Restaurant / Delivery / Order / Payment ...) should validate JWT

Create **the same `JwtAuthFilter` and `SecurityConfig`** in each microservice. Keep `security.jwt.secret` consistent across services if using HMAC. Example minimal steps:

1. Add jjwt dependencies (same as above) and `spring-boot-starter-security`.
2. Copy `JwtAuthFilter` and `SecurityConfig` (adjust routes and role mappings per service).
3. In controllers use `@PreAuthorize` with roles: `@PreAuthorize("hasRole('RESTAURANT')")` or `hasAnyRole('ADMIN','DELIVERY_BOY')`.
4. In service layer you can fetch the current user email with `Authentication auth = SecurityContextHolder.getContext().getAuthentication(); String email = auth.getName();`

Example role route mapping suggestions:
- **Restaurant service**: `/api/restaurant/**` -> hasRole('RESTAURANT')
- **Delivery service**: `/api/delivery/**` -> hasRole('DELIVERY_BOY')
- **Order service**: `/api/orders/customer/**` -> hasRole('CUSTOMER'), `/api/orders/restaurant/**` -> hasRole('RESTAURANT')
- **Admin endpoints**: `hasRole('ADMIN')`

---

## 11) Useful utility (get current user id if token has userId claim)

```java
// in any service
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

public Long getUserIdFromToken(String token) {
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    Claims claims = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
            .build()
            .parseClaimsJws(token)
            .getBody();
    Integer uid = claims.get("userId", Integer.class);
    return uid == null ? null : uid.longValue();
}
```

---

## 12) Quick testing tips

- Start Auth Service and ensure it issues tokens (include `userId` and `role` claims). Use Postman to call `/auth/login`.
- Put the `accessToken` in `Authorization: Bearer <token>` header when calling user-service endpoints.
- Check `SecurityContextHolder` inside endpoints to confirm the authenticated principal.

---

## 13) Next steps I can do for you

- Generate a full **Auth Service** that issues access + refresh tokens (complete code).  
- Generate **Restaurant / Delivery / Order** microservice skeletons with endpoints + JWT validation wired.  
- Provide Docker Compose for local run (Auth + Gateway + User + DB).  

Tell me which next step you want and I will generate it.
