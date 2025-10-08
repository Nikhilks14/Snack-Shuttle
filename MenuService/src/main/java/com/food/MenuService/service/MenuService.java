package com.food.MenuService.service;

import com.food.MenuService.dto.MenuDto;
import com.food.MenuService.dto.MenuItemDto;

import java.util.List;


public interface MenuService {
    MenuDto createMenu(MenuDto menuDto);
    MenuDto updateMenu(Long id, MenuDto menuDto);
    void deleteMenu(Long id);
    MenuDto getMenu(Long id);
    List<MenuDto> listMenus(boolean onlyActive);


    MenuItemDto addMenuItem(Long menuId, MenuItemDto menuItemDto);
    MenuItemDto updateMenuItem(Long id, MenuItemDto menuItemDto);
    void deleteMenuItem(Long id);
}