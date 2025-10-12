package com.food.authservice.client;

import com.food.authservice.Request.UserRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service") // matches service name registered in Eureka
public interface UserClient {

    @GetMapping("/api/users/{email}")
    String getUserByEmail(@PathVariable("email") String email);

    @PostMapping("/api/users")
    String createUser(@RequestBody UserRequest userRequest);
}
