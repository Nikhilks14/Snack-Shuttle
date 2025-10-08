package com.food.MenuService.controllers;

import com.food.menuservice.dto.MenuDto;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/v1/menus")
public class MenuController {


    private final MenuService menuService;


    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESTAURANT')")
    public ResponseEntity<MenuDto> createMenu(@Valid @RequestBody MenuDto dto) {
        MenuDto created = menuService.createMenu(dto);
        return ResponseEntity.created(URI.create("/api/v1/menus/" + created.getId())).body(created);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESTAURANT')")
    public ResponseEntity<MenuDto> updateMenu(@PathVariable Long id, @Valid @RequestBody MenuDto dto) {
        return ResponseEntity.ok(menuService.updateMenu(id, dto));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESTAURANT')")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<MenuDto> getMenu(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.getMenu(id));
    }


    @GetMapping
    public ResponseEntity<List<MenuDto>> listMenus(@RequestParam(defaultValue = "true") boolean onlyActive) {
        return ResponseEntity.ok(menuService.listMenus(onlyActive));
    }


    // Menu items
    @PostMapping("/{menuId}/items")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESTAURANT')")
    public ResponseEntity<MenuItemDto> addMenuItem(@PathVariable Long menuId, @Valid @RequestBody MenuItemDto dto) {
        MenuItemDto created = menuService.addMenuItem(menuId, dto);
        return ResponseEntity.created(URI.create("/api/v1/menus/" + menuId + "/items/" + created.getId())).body(created);
    }


    @PutMapping("/items/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESTAURANT')")
    public ResponseEntity<MenuItemDto> updateMenuItem(@PathVariable Long id, @Valid @RequestBody MenuItemDto dto) {
        return ResponseEntity.ok(menuService.updateMenuItem(id, dto));
    }


    @DeleteMapping("/items/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('RESTAURANT')")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuService.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }
}