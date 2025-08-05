package com.fiap.itmoura.tech_challenge_restaurant.application.usecases;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuItemWithContextDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantBasicResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantFullResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantRequest;
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
public class RestaurantUseCase {

    private final RestaurantRepository restaurantRepository;

    @Transactional
    public RestaurantFullResponse createRestaurant(RestaurantRequest restaurantRequest) {
        log.info("Creating restaurant: {}", restaurantRequest);

        List<MenuCategoryEntity> menuCategories = restaurantRequest.menu() != null 
            ? restaurantRequest.menu().stream()
                .map(this::convertToMenuCategoryEntity)
                .toList()
            : List.of();

        RestaurantEntity restaurantEntity = RestaurantEntity.builder()
            .id(UUID.randomUUID())
            .name(restaurantRequest.name())
            .address(restaurantRequest.address())
            .kitchenType(restaurantRequest.kitchenType().toEntity())
            .daysOperation(restaurantRequest.daysOperation())
            .ownerId(restaurantRequest.ownerId())
            .isActive(Boolean.TRUE.equals(restaurantRequest.isActive()) || restaurantRequest.isActive() == null)
            .menu(menuCategories)
            .lastUpdate(LocalDateTime.now())
            .createdAt(LocalDateTime.now())
            .build();

        var restaurantSaved = restaurantRepository.save(restaurantEntity);
        log.info("Restaurant created successfully with ID: {}", restaurantSaved.getId());

        return RestaurantFullResponse.fromEntity(restaurantSaved);
    }

    @Transactional
    public RestaurantFullResponse updateRestaurant(UUID id, RestaurantRequest restaurantRequest) {
        log.info("Updating restaurant with ID: {}", id);

        RestaurantEntity existingRestaurant = restaurantRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Restaurant not found with ID: " + id));

        List<MenuCategoryEntity> menuCategories = restaurantRequest.menu() != null 
            ? restaurantRequest.menu().stream()
                .map(this::convertToMenuCategoryEntity)
                .toList()
            : existingRestaurant.getMenu();

        RestaurantEntity updatedRestaurant = RestaurantEntity.builder()
            .id(existingRestaurant.getId())
            .name(restaurantRequest.name())
            .address(restaurantRequest.address())
            .kitchenType(restaurantRequest.kitchenType().toEntity())
            .daysOperation(restaurantRequest.daysOperation())
            .ownerId(restaurantRequest.ownerId())
            .isActive(restaurantRequest.isActive() != null ? restaurantRequest.isActive() : existingRestaurant.getIsActive())
            .menu(menuCategories)
            .lastUpdate(LocalDateTime.now())
            .createdAt(existingRestaurant.getCreatedAt())
            .build();

        var restaurantSaved = restaurantRepository.save(updatedRestaurant);
        log.info("Restaurant updated successfully with ID: {}", restaurantSaved.getId());

        return RestaurantFullResponse.fromEntity(restaurantSaved);
    }

    public List<RestaurantBasicResponse> getAllRestaurants() {
        log.info("Fetching all restaurants without menu");
        
        List<RestaurantEntity> restaurants = restaurantRepository.findAll();
        
        return restaurants.stream()
            .map(RestaurantBasicResponse::fromEntity)
            .toList();
    }

    public List<RestaurantBasicResponse> getAllActiveRestaurants() {
        log.info("Fetching all active restaurants without menu");
        
        List<RestaurantEntity> restaurants = restaurantRepository.findByIsActiveTrue();
        
        return restaurants.stream()
            .map(RestaurantBasicResponse::fromEntity)
            .toList();
    }

    public List<RestaurantFullResponse> getAllRestaurantsWithMenu() {
        log.info("Fetching all restaurants with full menu");
        
        List<RestaurantEntity> restaurants = restaurantRepository.findAll();
        
        return restaurants.stream()
            .map(RestaurantFullResponse::fromEntity)
            .toList();
    }

    public RestaurantFullResponse getRestaurantById(UUID id) {
        log.info("Fetching restaurant by ID: {}", id);
        
        RestaurantEntity restaurant = restaurantRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Restaurant not found with ID: " + id));
        
        return RestaurantFullResponse.fromEntity(restaurant);
    }

    public MenuItemWithContextDTO getMenuItemById(UUID itemId) {
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

    @Transactional
    public void deleteRestaurant(UUID id) {
        log.info("Deleting restaurant with ID: {}", id);
        
        if (!restaurantRepository.existsById(id)) {
            throw new NotFoundException("Restaurant not found with ID: " + id);
        }
        
        restaurantRepository.deleteById(id);
        log.info("Restaurant deleted successfully with ID: {}", id);
    }

    private MenuCategoryEntity convertToMenuCategoryEntity(com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuCategoryDTO categoryDTO) {
        List<MenuItemEntity> items = categoryDTO.getItems() != null 
            ? categoryDTO.getItems().stream()
                .map(itemDTO -> MenuItemEntity.builder()
                    .id(itemDTO.getId() != null ? itemDTO.getId() : UUID.randomUUID())
                    .name(itemDTO.getName())
                    .description(itemDTO.getDescription())
                    .price(itemDTO.getPrice())
                    .onlyForLocalConsumption(itemDTO.getOnlyForLocalConsumption())
                    .imagePath(itemDTO.getImagePath())
                    .isActive(itemDTO.getIsActive() != null ? itemDTO.getIsActive() : true)
                    .build())
                .toList()
            : List.of();

        return MenuCategoryEntity.builder()
            .id(categoryDTO.getId() != null ? categoryDTO.getId() : UUID.randomUUID())
            .type(categoryDTO.getType())
            .items(items)
            .build();
    }
}
