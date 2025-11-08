package com.food.CartService.dto;


import com.food.CartService.entity.type.BloodGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BloodGroupCountResponseEntity {

    private BloodGroup bloodGroupType;
    private Long count;
}