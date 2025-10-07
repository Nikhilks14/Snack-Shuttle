package com.food.RestaurantService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "restaurants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String phone;
    private Boolean active = true;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MenuItem> menuItems;

    @ElementCollection
    @CollectionTable(name = "restaurant_cuisines", joinColumns = @JoinColumn(name = "restaurant_id"))
    @Column(name = "cuisine")
    private List<String> cuisines;
}
