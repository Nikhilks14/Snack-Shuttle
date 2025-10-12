package com.food.authservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service") // registered name in Eureka
public interface UserClient {

    @GetMapping("/api/v1/users/{email}")
    String getUserByEmail(@PathVariable("email") String email);
}
