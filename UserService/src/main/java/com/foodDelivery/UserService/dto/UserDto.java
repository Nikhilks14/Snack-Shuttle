package com.foodDelivery.UserService.dto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private Role role;
}