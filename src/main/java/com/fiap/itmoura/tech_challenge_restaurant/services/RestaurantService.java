package com.fiap.itmoura.tech_challenge_restaurant.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fiap.itmoura.tech_challenge_restaurant.exception.NotFoundException;
import com.fiap.itmoura.tech_challenge_restaurant.model.entity.RestaurantEntity;
import com.fiap.itmoura.tech_challenge_restaurant.model.request.RestaurantRequest;
import com.fiap.itmoura.tech_challenge_restaurant.model.response.RestaurantResponse;
import com.fiap.itmoura.tech_challenge_restaurant.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor    
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final KitchenTypeService kitchenTypeService;

    @Transactional
    public RestaurantResponse createRestaurant(RestaurantRequest restaurantRequest) {
        log.info("Creating restaurant: {}", restaurantRequest);
        
        var kitchenType = kitchenTypeService.getKitchenTypeByIdOrName(restaurantRequest.kitchenType().toUpperCase());

        if (kitchenType == null) {
            throw new NotFoundException("Kitchen type not found");
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
}
