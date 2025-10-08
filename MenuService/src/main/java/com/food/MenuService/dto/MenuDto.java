package com.food.MenuService.dto;

import lombok.*;


import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDto {
    private Long id;
    private String title;
    private String description;
    private boolean active;
    private List<MenuItemDto> items;
}