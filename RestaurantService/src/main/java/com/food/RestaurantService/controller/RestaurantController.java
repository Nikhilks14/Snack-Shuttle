package com.food.RestaurantService.controller;

package com.food.restaurantservice.controller;

import com.food.restaurantservice.dto.*;
import com.food.restaurantservice.entity.Cuisine;
import com.food.restaurantservice.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    // Create (ADMIN or RESTAURANT_OWNER role)
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT')")
    public ResponseEntity<RestaurantDto> create(@Valid @RequestBody RestaurantDto dto) {
        RestaurantDto created = restaurantService.createRestaurant(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Update
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT')")
    public ResponseEntity<RestaurantDto> update(@PathVariable Long id, @RequestBody RestaurantDto dto) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(id, dto));
    }

    // Get by id (open)
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getById(id));
    }

    // List all (paginated)
    @GetMapping
    public ResponseEntity<Page<RestaurantDto>> list(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(required = false) String cuisine) {
        if (cuisine != null) {
            // accept comma separated
            Set<Cuisine> cuisines = new HashSet<>();
            for (String s : cuisine.split(",")) {
                try {
                    cuisines.add(Cuisine.valueOf(s.trim().toUpperCase()));
                } catch (Exception ignored) {}
            }
            return ResponseEntity.ok(restaurantService.findByCuisines(cuisines, page, size));
        } else {
            return ResponseEntity.ok(restaurantService.listAll(page, size));
        }
    }

    // List available
    @GetMapping("/available")
    public ResponseEntity<Page<RestaurantDto>> listAvailable(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(restaurantService.listAvailable(page, size));
    }

    // Set availability
    @PatchMapping("/{id}/availability")
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT')")
    public ResponseEntity<Void> setAvailability(@PathVariable Long id, @RequestBody AvailabilityDto dto) {
        restaurantService.setAvailability(id, dto.isAvailable());
        return ResponseEntity.noContent().build();
    }
}
