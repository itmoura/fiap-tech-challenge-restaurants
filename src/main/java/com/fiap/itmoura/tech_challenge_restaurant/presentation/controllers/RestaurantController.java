package com.fiap.itmoura.tech_challenge_restaurant.presentation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantBasicResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantFullResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantRequest;
import com.fiap.itmoura.tech_challenge_restaurant.application.usecases.RestaurantUseCase;
import com.fiap.itmoura.tech_challenge_restaurant.presentation.contracts.RestaurantControllerInterface;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController implements RestaurantControllerInterface {

    private final RestaurantUseCase restaurantService;

    @Override
    @PostMapping
    public ResponseEntity<RestaurantFullResponse> createRestaurant(
            @Valid @RequestBody RestaurantRequest restaurantRequest) {
        RestaurantFullResponse response = restaurantService.createRestaurant(restaurantRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<RestaurantBasicResponse>> getAllRestaurants() {
        List<RestaurantBasicResponse> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }

    @Override
    @GetMapping("/full")
    public ResponseEntity<List<RestaurantFullResponse>> getAllRestaurantsWithMenu() {
        List<RestaurantFullResponse> restaurants = restaurantService.getAllRestaurantsWithMenu();
        return ResponseEntity.ok(restaurants);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantFullResponse> getRestaurantById(@PathVariable String id) {
        RestaurantFullResponse restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(restaurant);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantFullResponse> updateRestaurant(
            @PathVariable String id,
            @Valid @RequestBody RestaurantRequest restaurantRequest) {
        RestaurantFullResponse response = restaurantService.updateRestaurant(id, restaurantRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable String id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}
