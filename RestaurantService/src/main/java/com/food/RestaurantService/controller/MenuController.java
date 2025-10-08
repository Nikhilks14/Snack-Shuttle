package com.food.RestaurantService.controller;

package com.food.restaurantservice.controller;

import com.food.restaurantservice.dto.*;
import com.food.restaurantservice.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants/{restaurantId}/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT')")
    public ResponseEntity<MenuDto> createMenu(@PathVariable Long restaurantId, @Valid @RequestBody MenuDto dto) {
        MenuDto created = menuService.createMenu(restaurantId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<MenuDto>> list(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(menuService.listMenusForRestaurant(restaurantId));
    }

    @PostMapping("/{menuId}/items")
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT')")
    public ResponseEntity<MenuDto> addItem(@PathVariable Long restaurantId, @PathVariable Long menuId, @RequestBody MenuItemDto itemDto) {
        // restaurantId is present in path for clarity; service will validate relation
        return ResponseEntity.ok(menuService.addMenuItem(menuId, itemDto));
    }

    @GetMapping("/{menuId}")
    public ResponseEntity<MenuDto> getMenu(@PathVariable Long restaurantId, @PathVariable Long menuId) {
        return ResponseEntity.ok(menuService.getMenu(menuId));
    }
}
