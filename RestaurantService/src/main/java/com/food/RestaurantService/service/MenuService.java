package com.food.RestaurantService.service;

package com.food.restaurantservice.service;

import com.food.RestaurantService.dto.MenuDto;
import com.food.RestaurantService.dto.MenuItemDto;
import com.food.RestaurantService.repo.MenuItemRepository;
import com.food.RestaurantService.repo.MenuRepository;
import com.food.RestaurantService.security.RestaurantRepository;
import com.food.restaurantservice.dto.*;
import com.food.restaurantservice.entity.*;
import com.food.restaurantservice.exception.ResourceNotFoundException;
import com.food.restaurantservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public MenuDto createMenu(Long restaurantId, MenuDto dto) {
        Restaurant r = restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Restaurant", "id", restaurantId));
        Menu menu = Menu.builder().title(dto.getTitle()).restaurant(r).build();
        if (dto.getItems() != null) {
            List<MenuItem> items = dto.getItems().stream().map(it -> MenuItem.builder()
                    .name(it.getName())
                    .description(it.getDescription())
                    .price(it.getPrice())
                    .available(it.isAvailable())
                    .menu(menu)
                    .build()).collect(Collectors.toList());
            menu.setItems(items);
        }
        menu = menuRepository.save(menu);
        return toDto(menu);
    }

    public MenuDto addMenuItem(Long menuId, MenuItemDto itemDto) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu", "id", menuId));
        MenuItem item = MenuItem.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .price(itemDto.getPrice())
                .available(itemDto.isAvailable())
                .menu(menu)
                .build();
        item = menuItemRepository.save(item);
        menu.getItems().add(item);
        menuRepository.save(menu);
        return toDto(menu);
    }

    public MenuDto getMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu", "id", menuId));
        return toDto(menu);
    }

    public List<MenuDto> listMenusForRestaurant(Long restaurantId) {
        List<Menu> menus = menuRepository.findByRestaurantId(restaurantId);
        return menus.stream().map(this::toDto).collect(Collectors.toList());
    }

    private MenuDto toDto(Menu m) {
        List<MenuItemDto> items = m.getItems() == null ? Collections.emptyList() :
                m.getItems().stream().map(it -> MenuItemDto.builder()
                        .id(it.getId())
                        .name(it.getName())
                        .description(it.getDescription())
                        .price(it.getPrice())
                        .available(it.isAvailable())
                        .build()).collect(Collectors.toList());

        return MenuDto.builder()
                .id(m.getId())
                .title(m.getTitle())
                .restaurantId(m.getRestaurant() != null ? m.getRestaurant().getId() : null)
                .items(items)
                .build();
    }
}
