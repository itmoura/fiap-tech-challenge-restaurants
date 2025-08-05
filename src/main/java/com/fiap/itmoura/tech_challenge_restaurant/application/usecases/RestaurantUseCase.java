package com.fiap.itmoura.tech_challenge_restaurant.application.usecases;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantRequest;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.RestaurantRepository;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.RestaurantEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor    
public class RestaurantUseCase {

    private final RestaurantRepository restaurantRepository;
    private final KitchenTypeUseCase kitchenTypeService;
    private final UserUseCase userUseCase;

    @Transactional
    public RestaurantResponse createRestaurant(RestaurantRequest restaurantRequest) {
        log.info("Creating restaurant: {}", restaurantRequest);
        
        var kitchenType = kitchenTypeService.getKitchenTypeByIdOrName(restaurantRequest.kitchenType().toUpperCase());

        if (kitchenType == null) {
            throw new NotFoundException("Kitchen type not found");
        }

        // Validar se o usuário (dono) existe
        if (restaurantRequest.ownerId() != null && !restaurantRequest.ownerId().trim().isEmpty()) {
            userUseCase.getUserById(restaurantRequest.ownerId());
        }

        RestaurantEntity restaurantEntity = RestaurantEntity.builder()
            .name(restaurantRequest.name())
            .kitchenType(kitchenType)
            .address(restaurantRequest.address())
            .daysOperation(restaurantRequest.daysOperation())
            .ownerId(restaurantRequest.ownerId())
            .isActive(Boolean.TRUE.equals(restaurantRequest.isActive()) || restaurantRequest.isActive() == null)
            .rating(restaurantRequest.rating())
            .lastUpdate(LocalDateTime.now())
            .createdAt(LocalDateTime.now())
            .build();

        var restaurantSaved = restaurantRepository.save(restaurantEntity);

        return RestaurantResponse.fromEntity(restaurantSaved);
    }

    public RestaurantResponse getRestaurantById(String id) {
        log.info("Getting restaurant by id: {}", id);
        return RestaurantResponse.fromEntity(restaurantRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Restaurant not found")));
    }

    public List<RestaurantResponse> getAllRestaurants() {
        log.info("Getting all restaurants");
        return restaurantRepository.findByIsActiveTrue().stream()
            .map(RestaurantResponse::fromEntity)
            .collect(Collectors.toList());
    }

    public RestaurantResponse updateRestaurant(String id, RestaurantRequest restaurantRequest) {
        log.info("Updating restaurant: {}", restaurantRequest);
        RestaurantEntity restaurantEntity = restaurantRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        checkWhatRestaurantChanging(restaurantRequest, restaurantEntity);

        restaurantEntity.setName(restaurantRequest.name());
        restaurantEntity.setKitchenType(kitchenTypeService.getKitchenTypeByIdOrName(restaurantRequest.kitchenType().toUpperCase()));
        restaurantEntity.setAddress(restaurantRequest.address());
        restaurantEntity.setDaysOperation(restaurantRequest.daysOperation());
        
        // Validar se o usuário (dono) existe quando for alterado
        if (restaurantRequest.ownerId() != null && !restaurantRequest.ownerId().trim().isEmpty()) {
            userUseCase.getUserById(restaurantRequest.ownerId());
        }
        restaurantEntity.setOwnerId(restaurantRequest.ownerId());
        
        restaurantEntity.setIsActive(Boolean.TRUE.equals(restaurantRequest.isActive()) || restaurantRequest.isActive() == null);
        restaurantEntity.setRating(restaurantRequest.rating());
        restaurantEntity.setLastUpdate(LocalDateTime.now());

        var restaurantSaved = restaurantRepository.save(restaurantEntity);

        return RestaurantResponse.fromEntity(restaurantSaved);
    }

    public void deleteRestaurant(String id) {
        log.info("Deleting restaurant: {}", id);
        RestaurantEntity restaurantEntity = restaurantRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));
        restaurantRepository.delete(restaurantEntity);
    }

    public void disableRestaurant(String id) {
        log.info("Disabling restaurant: {}", id);
        RestaurantEntity restaurantEntity = restaurantRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));
        restaurantEntity.setIsActive(false);
        restaurantEntity.setLastUpdate(LocalDateTime.now());
        restaurantRepository.save(restaurantEntity);
    }

    private void checkWhatRestaurantChanging(RestaurantRequest restaurantRequest, RestaurantEntity restaurantEntity) {
        if (Objects.nonNull(restaurantEntity.getName()) && !restaurantEntity.getName().equals(restaurantRequest.name())) {
            restaurantEntity.setName(restaurantRequest.name());
        }
        if (Objects.nonNull(restaurantEntity.getKitchenType()) && !restaurantEntity.getKitchenType().getName().equals(restaurantRequest.kitchenType())) {
            restaurantEntity.setKitchenType(kitchenTypeService.getKitchenTypeByIdOrName(restaurantRequest.kitchenType().toUpperCase()));
        }
        if (Objects.nonNull(restaurantEntity.getAddress()) && !restaurantEntity.getAddress().equals(restaurantRequest.address())) {
            restaurantEntity.setAddress(restaurantRequest.address());
        }
        if (Objects.nonNull(restaurantEntity.getDaysOperation()) && !restaurantEntity.getDaysOperation().equals(restaurantRequest.daysOperation())) {
            restaurantEntity.setDaysOperation(restaurantRequest.daysOperation());
        }
        if (Objects.nonNull(restaurantEntity.getOwnerId()) && !restaurantEntity.getOwnerId().equals(restaurantRequest.ownerId())) {
            // Validar se o usuário (dono) existe quando for alterado
            if (restaurantRequest.ownerId() != null && !restaurantRequest.ownerId().trim().isEmpty()) {
                userUseCase.getUserById(restaurantRequest.ownerId());
            }
            restaurantEntity.setOwnerId(restaurantRequest.ownerId());
        }
        if (Objects.nonNull(restaurantEntity.getIsActive()) && !restaurantEntity.getIsActive().equals(restaurantRequest.isActive())) {
            restaurantEntity.setIsActive(Boolean.TRUE.equals(restaurantRequest.isActive()) || restaurantRequest.isActive() == null);
        }
        if (Objects.nonNull(restaurantEntity.getRating()) && !restaurantEntity.getRating().equals(restaurantRequest.rating())) {
            restaurantEntity.setRating(restaurantRequest.rating());
        }
        restaurantEntity.setLastUpdate(LocalDateTime.now());
    }
}
