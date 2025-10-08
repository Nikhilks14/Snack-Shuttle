package com.food.MenuService.dto;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemDto {
    private Long id;
    private String name;
    private String description;
    private double price;
    private String category;
    private boolean available;
}