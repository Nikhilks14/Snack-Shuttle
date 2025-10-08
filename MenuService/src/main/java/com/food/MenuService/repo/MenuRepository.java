package com.food.MenuService.repo;

import com.food.MenuService.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;


@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByActiveTrue();
}