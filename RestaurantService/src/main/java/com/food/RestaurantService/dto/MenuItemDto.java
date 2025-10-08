package com.food.RestaurantService.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemDto {
    private Long id;
    private String name;
    private String description;
    private double price;
    private boolean available;
}
