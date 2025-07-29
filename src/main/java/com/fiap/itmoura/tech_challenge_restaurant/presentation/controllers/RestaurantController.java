package com.fiap.itmoura.tech_challenge_restaurant.presentation.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantRequest;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.usecases.RestaurantUseCase;
import com.fiap.itmoura.tech_challenge_restaurant.presentation.contracts.RestaurantControllerInterfaces;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController implements RestaurantControllerInterfaces {

    private final RestaurantUseCase restaurantService;

    @Override
    public RestaurantResponse createRestaurant(RestaurantRequest restaurantRequest) {
        return restaurantService.createRestaurant(restaurantRequest);
    }

    @Override
    public RestaurantResponse getRestaurantById(String id) {
        return restaurantService.getRestaurantById(id);
    }

    @Override
    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }

    @Override
    public RestaurantResponse updateRestaurant(String id, RestaurantRequest restaurantRequest) {
        return restaurantService.updateRestaurant(id, restaurantRequest);
    }
    
    @Override
    public void deleteRestaurant(String id) {
        restaurantService.deleteRestaurant(id);
    }

    @Override
    public void disableRestaurant(String id) {
        restaurantService.disableRestaurant(id);
    }
}
