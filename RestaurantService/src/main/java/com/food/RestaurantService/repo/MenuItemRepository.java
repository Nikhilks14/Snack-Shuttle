package com.food.RestaurantService.repo;

package com.example.food.restaurant.repository;

import com.example.food.restaurant.model.MenuItem;
import com.example.food.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByRestaurant(Restaurant restaurant);
    List<MenuItem> findByCategoryIgnoreCase(String category);
    List<MenuItem> findByAvailableTrue();
}
