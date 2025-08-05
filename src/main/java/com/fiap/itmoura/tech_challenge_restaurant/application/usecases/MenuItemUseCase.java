package com.fiap.itmoura.tech_challenge_restaurant.application.usecases;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuItemRequest;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuItemResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuItemWithContextDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.RestaurantRepository;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.MenuCategoryEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.MenuItemEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.RestaurantEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class MenuItemUseCase {

    private final RestaurantRepository restaurantRepository;

    @Transactional
    public MenuItemResponse createMenuItem(String restaurantId, String menuId, MenuItemRequest request) {
        log.info("Creating menu item for restaurant ID: {} and menu ID: {}", restaurantId, menuId);

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new NotFoundException("Restaurant not found with ID: " + restaurantId));

        MenuCategoryEntity category = findMenuCategory(restaurant, menuId);

        MenuItemEntity newItem = MenuItemEntity.builder()
            .id(UUID.randomUUID().toString())
            .name(request.getName())
            .description(request.getDescription())
            .price(request.getPrice())
            .onlyForLocalConsumption(request.getOnlyForLocalConsumption() != null ? request.getOnlyForLocalConsumption() : false)
            .imagePath(request.getImagePath())
            .isActive(request.getIsActive() != null ? request.getIsActive() : true)
            .build();

        List<MenuItemEntity> currentItems = category.getItems() != null 
            ? category.getItems() 
            : List.of();

        List<MenuItemEntity> updatedItems = new java.util.ArrayList<>(currentItems);
        updatedItems.add(newItem);

        category.setItems(updatedItems);

        RestaurantEntity savedRestaurant = restaurantRepository.save(restaurant);

        log.info("Menu item created successfully with ID: {}", newItem.getId());
        return MenuItemResponse.fromEntity(newItem, savedRestaurant.getId(), category.getId());
    }

    @Transactional
    public MenuItemResponse updateMenuItem(String restaurantId, String menuId, String itemId, MenuItemRequest request) {
        log.info("Updating menu item ID: {} for restaurant ID: {} and menu ID: {}", itemId, restaurantId, menuId);

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new NotFoundException("Restaurant not found with ID: " + restaurantId));

        MenuCategoryEntity category = findMenuCategory(restaurant, menuId);
        MenuItemEntity itemToUpdate = findMenuItem(category, itemId);

        itemToUpdate.setName(request.getName());
        itemToUpdate.setDescription(request.getDescription());
        itemToUpdate.setPrice(request.getPrice());
        itemToUpdate.setOnlyForLocalConsumption(request.getOnlyForLocalConsumption() != null ? request.getOnlyForLocalConsumption() : itemToUpdate.getOnlyForLocalConsumption());
        itemToUpdate.setImagePath(request.getImagePath());
        itemToUpdate.setIsActive(request.getIsActive() != null ? request.getIsActive() : itemToUpdate.getIsActive());

        RestaurantEntity savedRestaurant = restaurantRepository.save(restaurant);

        log.info("Menu item updated successfully with ID: {}", itemId);
        return MenuItemResponse.fromEntity(itemToUpdate, savedRestaurant.getId(), category.getId());
    }

    @Transactional
    public void deleteMenuItem(String restaurantId, String menuId, String itemId) {
        log.info("Deleting menu item ID: {} for restaurant ID: {} and menu ID: {}", itemId, restaurantId, menuId);

        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new NotFoundException("Restaurant not found with ID: " + restaurantId));

        MenuCategoryEntity category = findMenuCategory(restaurant, menuId);

        List<MenuItemEntity> items = category.getItems();
        if (items == null || items.isEmpty()) {
            throw new NotFoundException("No items found in menu category ID: " + menuId);
        }

        boolean itemExists = items.stream()
            .anyMatch(item -> item.getId().equals(itemId));

        if (!itemExists) {
            throw new NotFoundException("Menu item not found with ID: " + itemId);
        }

        List<MenuItemEntity> updatedItems = items.stream()
            .filter(item -> !item.getId().equals(itemId))
            .toList();

        category.setItems(updatedItems);
        restaurantRepository.save(restaurant);

        log.info("Menu item deleted successfully with ID: {}", itemId);
    }

    public MenuItemWithContextDTO getMenuItemById(String itemId) {
        log.info("Fetching menu item by ID: {}", itemId);

        RestaurantEntity restaurant = restaurantRepository.findByMenuItemId(itemId)
            .orElseThrow(() -> new NotFoundException("Menu item not found with ID: " + itemId));

        // Encontrar o item e sua categoria
        for (MenuCategoryEntity category : restaurant.getMenu()) {
            for (MenuItemEntity item : category.getItems()) {
                if (item.getId().equals(itemId)) {
                    return MenuItemWithContextDTO.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .description(item.getDescription())
                        .price(item.getPrice())
                        .onlyForLocalConsumption(item.getOnlyForLocalConsumption())
                        .imagePath(item.getImagePath())
                        .isActive(item.getIsActive())
                        .category(MenuItemWithContextDTO.MenuCategoryContextDTO.builder()
                            .id(category.getId())
                            .type(category.getType())
                            .build())
                        .restaurant(MenuItemWithContextDTO.RestaurantContextDTO.builder()
                            .id(restaurant.getId())
                            .name(restaurant.getName())
                            .address(restaurant.getAddress())
                            .build())
                        .build();
                }
            }
        }

        throw new NotFoundException("Menu item not found with ID: " + itemId);
    }

    private MenuCategoryEntity findMenuCategory(RestaurantEntity restaurant, String menuId) {
        List<MenuCategoryEntity> menu = restaurant.getMenu();
        if (menu == null || menu.isEmpty()) {
            throw new NotFoundException("Menu not found for restaurant ID: " + restaurant.getId());
        }

        return menu.stream()
            .filter(category -> category.getId().equals(menuId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Menu category not found with ID: " + menuId));
    }

    private MenuItemEntity findMenuItem(MenuCategoryEntity category, String itemId) {
        List<MenuItemEntity> items = category.getItems();
        if (items == null || items.isEmpty()) {
            throw new NotFoundException("No items found in menu category ID: " + category.getId());
        }

        return items.stream()
            .filter(item -> item.getId().equals(itemId))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Menu item not found with ID: " + itemId));
    }
}
