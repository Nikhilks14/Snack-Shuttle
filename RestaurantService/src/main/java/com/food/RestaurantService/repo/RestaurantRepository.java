package com.food.RestaurantService.repo;

package com.example.food.restaurant.repository;

import com.example.food.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByActiveTrue();
    List<Restaurant> findByCuisinesContaining(String cuisine);
    List<Restaurant> findByNameContainingIgnoreCase(String name);
}
