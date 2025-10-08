package com.food.RestaurantService.dto;

import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDto {
    private Long id;
    private String title;
    private Long restaurantId;
    private List<MenuItemDto> items;
}
