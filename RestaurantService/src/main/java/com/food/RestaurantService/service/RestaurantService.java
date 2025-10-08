package com.food.RestaurantService.service;

import com.food.RestaurantService.dto.RestaurantDto;
import com.food.RestaurantService.entity.Restaurant;
import com.food.RestaurantService.repo.MenuRepository;
import com.food.RestaurantService.security.RestaurantRepository;
import com.food.restaurantservice.dto.*;
import com.food.restaurantservice.entity.*;
import com.food.restaurantservice.exception.ResourceNotFoundException;
import com.food.restaurantservice.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;

    public RestaurantDto createRestaurant(RestaurantDto dto) {
        Restaurant r = Restaurant.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .available(dto.isAvailable())
                .cuisines(dto.getCuisines() == null ? new HashSet<>() : dto.getCuisines())
                .build();
        r = restaurantRepository.save(r);
        return toDto(r);
    }

    public RestaurantDto updateRestaurant(Long id, RestaurantDto dto) {
        Restaurant r = restaurantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Restaurant", "id", id));
        if (dto.getName() != null) r.setName(dto.getName());
        if (dto.getAddress() != null) r.setAddress(dto.getAddress());
        r.setAvailable(dto.isAvailable());
        if (dto.getCuisines() != null) r.setCuisines(dto.getCuisines());
        r = restaurantRepository.save(r);
        return toDto(r);
    }

    public RestaurantDto getById(Long id) {
        Restaurant r = restaurantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Restaurant", "id", id));
        return toDto(r);
    }

    public Page<RestaurantDto> listAll(int page, int size) {
        Page<Restaurant> p = restaurantRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
        return p.map(this::toDto);
    }

    public Page<RestaurantDto> listAvailable(int page, int size) {
        Page<Restaurant> p = restaurantRepository.findByAvailableTrue(PageRequest.of(page, size));
        return p.map(this::toDto);
    }

    public Page<RestaurantDto> findByCuisines(Set<Cuisine> cuisines, int page, int size) {
        Page<Restaurant> p = restaurantRepository.findByCuisinesIn(cuisines, PageRequest.of(page, size));
        return p.map(this::toDto);
    }

    @Transactional
    public void setAvailability(Long id, boolean available) {
        Restaurant r = restaurantRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Restaurant", "id", id));
        r.setAvailable(available);
        // menus/items not deleted; just availability flag on restaurant controls visibility
        restaurantRepository.save(r);
    }

    private RestaurantDto toDto(Restaurant r) {
        return RestaurantDto.builder()
                .id(r.getId())
                .name(r.getName())
                .address(r.getAddress())
                .available(r.isAvailable())
                .cuisines(r.getCuisines())
                .build();
    }
}
