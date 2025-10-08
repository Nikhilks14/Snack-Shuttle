package com.food.RestaurantService.dto;

import com.food.RestaurantService.entity.Cuisine;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDto {
    private Long id;
    private String name;
    private String address;
    private boolean available;
    private Set<Cuisine> cuisines;
}
