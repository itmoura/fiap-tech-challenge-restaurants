package com.fiap.itmoura.tech_challenge_restaurant.application.usecases;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuCategoryRequest;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuCategoryResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.RestaurantRepository;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.MenuCategoryEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.RestaurantEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class MenuUseCase {

    private final RestaurantRepository restaurantRepository;

    @Transactional
    public MenuCategoryResponse createMenuCategory(String restaurantId, MenuCategoryRequest request) {
        log.info("Creating menu category for restaurant ID: {}", restaurantId);

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new NotFoundException("Restaurant not found with ID: " + restaurantId));

        MenuCategoryEntity newCategory = MenuCategoryEntity.builder()
            .id(UUID.randomUUID().toString())
            .type(request.getType())
            .items(List.of())
            .build();

        List<MenuCategoryEntity> currentMenu = restaurant.getMenu() != null 
            ? restaurant.getMenu() 
            : List.of();

        List<MenuCategoryEntity> updatedMenu = new java.util.ArrayList<>(currentMenu);
        updatedMenu.add(newCategory);

        restaurant.setMenu(updatedMenu);
        RestaurantEntity savedRestaurant = restaurantRepository.save(restaurant);

        log.info("Menu category created successfully with ID: {}", newCategory.getId());
        return MenuCategoryResponse.fromEntity(newCategory, savedRestaurant.getId());
    }

    @Transactional
    public MenuCategoryResponse updateMenuCategory(String restaurantId, String menuId, MenuCategoryRequest request) {
        log.info("Updating menu category ID: {} for restaurant ID: {}", menuId, restaurantId);

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new NotFoundException("Restaurant not found with ID: " + restaurantId));

        List<MenuCategoryEntity> menu = restaurant.getMenu();
        if (menu == null || menu.isEmpty()) {
            throw new NotFoundException("Menu not found for restaurant ID: " + restaurantId);
        }

        MenuCategoryEntity categoryToUpdate = menu.stream()
            .filter(category -> category.getId().equals(menuId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Menu category not found with ID: " + menuId));

        categoryToUpdate.setType(request.getType());

        RestaurantEntity savedRestaurant = restaurantRepository.save(restaurant);

        log.info("Menu category updated successfully with ID: {}", menuId);
        return MenuCategoryResponse.fromEntity(categoryToUpdate, savedRestaurant.getId());
    }

    @Transactional
    public void deleteMenuCategory(String restaurantId, String menuId) {
        log.info("Deleting menu category ID: {} for restaurant ID: {}", menuId, restaurantId);

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new NotFoundException("Restaurant not found with ID: " + restaurantId));

        List<MenuCategoryEntity> menu = restaurant.getMenu();
        if (menu == null || menu.isEmpty()) {
            throw new NotFoundException("Menu not found for restaurant ID: " + restaurantId);
        }

        boolean categoryExists = menu.stream()
            .anyMatch(category -> category.getId().equals(menuId));

        if (!categoryExists) {
            throw new NotFoundException("Menu category not found with ID: " + menuId);
        }

        List<MenuCategoryEntity> updatedMenu = menu.stream()
            .filter(category -> !category.getId().equals(menuId))
            .toList();

        restaurant.setMenu(updatedMenu);
        restaurantRepository.save(restaurant);

        log.info("Menu category deleted successfully with ID: {}", menuId);
    }

    public MenuCategoryResponse getMenuCategory(String restaurantId, String menuId) {
        log.info("Fetching menu category ID: {} for restaurant ID: {}", menuId, restaurantId);

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new NotFoundException("Restaurant not found with ID: " + restaurantId));

        List<MenuCategoryEntity> menu = restaurant.getMenu();
        if (menu == null || menu.isEmpty()) {
            throw new NotFoundException("Menu not found for restaurant ID: " + restaurantId);
        }

        MenuCategoryEntity category = menu.stream()
            .filter(cat -> cat.getId().equals(menuId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Menu category not found with ID: " + menuId));

        return MenuCategoryResponse.fromEntity(category, restaurant.getId());
    }
}
