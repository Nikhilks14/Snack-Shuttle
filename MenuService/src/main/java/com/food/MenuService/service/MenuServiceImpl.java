package com.food.MenuService.service;

import com.food.menuservice.dto.MenuDto;


@Override
public List<MenuDto> listMenus(boolean onlyActive) {
    List<Menu> menus = onlyActive ? menuRepository.findByActiveTrue() : menuRepository.findAll();
    return menus.stream().map(this::toDto).collect(Collectors.toList());
}


@Override
public MenuItemDto addMenuItem(Long menuId, MenuItemDto menuItemDto) {
    Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new ResourceNotFoundException("Menu", "id", menuId));
    MenuItem item = MenuItem.builder()
            .name(menuItemDto.getName())
            .description(menuItemDto.getDescription())
            .price(menuItemDto.getPrice())
            .category(menuItemDto.getCategory())
            .available(menuItemDto.isAvailable())
            .menu(menu)
            .build();
    MenuItem saved = menuItemRepository.save(item);
    menu.getItems().add(saved);
    menuRepository.save(menu);
    return toItemDto(saved);
}


@Override
public MenuItemDto updateMenuItem(Long id, MenuItemDto menuItemDto) {
    MenuItem item = menuItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MenuItem", "id", id));
    item.setName(menuItemDto.getName());
    item.setDescription(menuItemDto.getDescription());
    item.setPrice(menuItemDto.getPrice());
    item.setCategory(menuItemDto.getCategory());
    item.setAvailable(menuItemDto.isAvailable());
    MenuItem saved = menuItemRepository.save(item);
    return toItemDto(saved);
}


@Override
public void deleteMenuItem(Long id) {
    MenuItem item = menuItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MenuItem", "id", id));
    menuItemRepository.delete(item);
}


private MenuDto toDto(Menu menu) {
    return MenuDto.builder()
            .id(menu.getId())
            .title(menu.getTitle())
            .description(menu.getDescription())
            .active(menu.isActive())
            .items(menu.getItems().stream().map(this::toItemDto).collect(Collectors.toList()))
            .build();
}


private MenuItemDto toItemDto(MenuItem item) {
    return MenuItemDto.builder()
            .id(item.getId())
            .name(item.getName())
            .description(item.getDescription())
            .price(item.getPrice())
            .category(item.getCategory())
            .available(item.isAvailable())
            .build();
}
}