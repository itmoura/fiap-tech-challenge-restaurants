package com.fiap.itmoura.tech_challenge_restaurant.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.itmoura.tech_challenge_restaurant.controller.interfaces.RestaurantControllerInterfaces;
import com.fiap.itmoura.tech_challenge_restaurant.model.request.RestaurantRequest;
import com.fiap.itmoura.tech_challenge_restaurant.model.response.RestaurantResponse;
import com.fiap.itmoura.tech_challenge_restaurant.services.RestaurantService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController implements RestaurantControllerInterfaces {

    private final RestaurantService restaurantService;

    @Override
    public RestaurantResponse createRestaurant(RestaurantRequest restaurantRequest) {
        return restaurantService.createRestaurant(restaurantRequest);
    }
}
