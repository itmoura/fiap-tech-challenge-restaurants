package com.fiap.itmoura.tech_challenge_restaurant.application.usecases;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype.KitchenTypeResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantBasicResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantFullResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantRequest;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.RestaurantRepository;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.KitchenTypeEntity;
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
    private final KitchenTypeUseCase kitchenTypeUseCase;

    @Transactional
    public RestaurantFullResponse createRestaurant(RestaurantRequest restaurantRequest) {
        log.info("Creating restaurant: {}", restaurantRequest);

        // Buscar o tipo de cozinha
        KitchenTypeEntity kitchenType = getKitchenTypeFromRequest(restaurantRequest);

        List<MenuCategoryEntity> menuCategories = restaurantRequest.menu() != null 
            ? restaurantRequest.menu().stream()
                .map(this::convertToMenuCategoryEntity)
                .toList()
            : List.of();

        RestaurantEntity restaurantEntity = RestaurantEntity.builder()
            .id(UUID.randomUUID().toString())
            .name(restaurantRequest.name())
            .address(restaurantRequest.address())
            .kitchenType(kitchenType)
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
    public RestaurantFullResponse updateRestaurant(String id, RestaurantRequest restaurantRequest) {
        log.info("Updating restaurant with ID: {}", id);

        RestaurantEntity existingRestaurant = restaurantRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Restaurant not found with ID: " + id));

        // Buscar o tipo de cozinha
        KitchenTypeEntity kitchenType = getKitchenTypeFromRequest(restaurantRequest);

        List<MenuCategoryEntity> menuCategories = restaurantRequest.menu() != null 
            ? restaurantRequest.menu().stream()
                .map(this::convertToMenuCategoryEntity)
                .toList()
            : existingRestaurant.getMenu();

        RestaurantEntity updatedRestaurant = RestaurantEntity.builder()
            .id(existingRestaurant.getId())
            .name(restaurantRequest.name())
            .address(restaurantRequest.address())
            .kitchenType(kitchenType)
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

    @Transactional
    public RestaurantFullResponse disableRestaurant(String id) {
        log.info("Disabling restaurant with ID: {}", id);

        RestaurantEntity existingRestaurant = restaurantRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Restaurant not found with ID: " + id));

        RestaurantEntity disabledRestaurant = RestaurantEntity.builder()
            .id(existingRestaurant.getId())
            .name(existingRestaurant.getName())
            .address(existingRestaurant.getAddress())
            .kitchenType(existingRestaurant.getKitchenType())
            .daysOperation(existingRestaurant.getDaysOperation())
            .ownerId(existingRestaurant.getOwnerId())
            .isActive(false)
            .menu(existingRestaurant.getMenu())
            .lastUpdate(LocalDateTime.now())
            .createdAt(existingRestaurant.getCreatedAt())
            .build();

        var restaurantSaved = restaurantRepository.save(disabledRestaurant);
        log.info("Restaurant disabled successfully with ID: {}", restaurantSaved.getId());

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
        
        List<RestaurantEntity> restaurants = restaurantRepository.findAll();
        
        return restaurants.stream()
            .filter(RestaurantEntity::getIsActive)
            .map(RestaurantBasicResponse::fromEntity)
            .toList();
    }

    public List<RestaurantFullResponse> getAllRestaurantsWithMenu() {
        log.info("Fetching all restaurants with menu");
        
        List<RestaurantEntity> restaurants = restaurantRepository.findAll();
        
        return restaurants.stream()
            .map(RestaurantFullResponse::fromEntity)
            .toList();
    }

    public RestaurantFullResponse getRestaurantById(String id) {
        log.info("Fetching restaurant by ID: {}", id);
        
        RestaurantEntity restaurant = restaurantRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Restaurant not found with ID: " + id));
        
        return RestaurantFullResponse.fromEntity(restaurant);
    }

    @Transactional
    public void deleteRestaurant(String id) {
        log.info("Deleting restaurant with ID: {}", id);
        
        if (!restaurantRepository.existsById(id)) {
            throw new NotFoundException("Restaurant not found with ID: " + id);
        }
        
        restaurantRepository.deleteById(id);
        log.info("Restaurant deleted successfully with ID: {}", id);
    }

    private KitchenTypeEntity getKitchenTypeFromRequest(RestaurantRequest restaurantRequest) {
        if (restaurantRequest.kitchenType() == null) {
            throw new IllegalArgumentException("Kitchen type is required");
        }

        KitchenTypeResponse kitchenTypeResponse = new KitchenTypeResponse();

        // Se o kitchenType tem um ID, buscar por ID
        if (restaurantRequest.kitchenType().id() != null) {
            kitchenTypeResponse = kitchenTypeUseCase.getKitchenTypeById(restaurantRequest.kitchenType().id());
        }

        // Se não tem ID, buscar por nome
        if (restaurantRequest.kitchenType().name() != null) {
            kitchenTypeResponse = kitchenTypeUseCase.getKitchenTypeByIdOrName(restaurantRequest.kitchenType().name());
        }

        if (kitchenTypeResponse != null) {
            return KitchenTypeEntity.builder()
                .id(kitchenTypeResponse.getId())
                .name(kitchenTypeResponse.getName())
                .description(kitchenTypeResponse.getDescription())
                .createdAt(kitchenTypeResponse.getCreatedAt())
                .lastUpdate(kitchenTypeResponse.getLastUpdate())
                .build();
        }

        throw new IllegalArgumentException("Kitchen type must have either id or name");
    }

    private MenuCategoryEntity convertToMenuCategoryEntity(com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuCategoryDTO categoryDTO) {
        List<MenuItemEntity> menuItems = categoryDTO.getItems() != null 
            ? categoryDTO.getItems().stream()
                .map(this::convertToMenuItemEntity)
                .toList()
            : List.of();

        return MenuCategoryEntity.builder()
            .id(categoryDTO.getId() != null ? categoryDTO.getId().toString() : UUID.randomUUID().toString())
            .type(categoryDTO.getType())
            .items(menuItems)
            .build();
    }

    private MenuItemEntity convertToMenuItemEntity(com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuItemNestedDTO itemDTO) {
        return MenuItemEntity.builder()
            .id(itemDTO.getId() != null ? itemDTO.getId().toString() : UUID.randomUUID().toString())
            .name(itemDTO.getName())
            .description(itemDTO.getDescription())
            .price(itemDTO.getPrice())
            .onlyForLocalConsumption(Boolean.TRUE.equals(itemDTO.getOnlyForLocalConsumption()))
            .imagePath(itemDTO.getImagePath())
            .isActive(Boolean.TRUE.equals(itemDTO.getIsActive()))
            .build();
    }
}
