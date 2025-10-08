package com.food.RestaurantService.security;

import com.food.restaurantservice.entity.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Page<Restaurant> findByAvailableTrue(Pageable pageable);
    Page<Restaurant> findByCuisinesIn(Set<Cuisine> cuisines, Pageable pageable);
    List<Restaurant> findByNameContainingIgnoreCase(String name);
}
