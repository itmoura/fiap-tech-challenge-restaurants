package com.fiap.itmoura.tech_challenge_restaurant.presentation.contracts;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnCreateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnUpdateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantRequest;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface RestaurantControllerInterfaces {

    @PostMapping("/create")
    @Operation(summary = "Create a new restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Restaurant created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    RestaurantResponse createRestaurant(@Validated(OnCreateGroup.class) @RequestBody RestaurantRequest restaurantRequest);  
    
    @GetMapping("/{id}")
    @Operation(summary = "Get a restaurant by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurant retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @ResponseStatus(HttpStatus.OK)
    RestaurantResponse getRestaurantById(@PathVariable String id);

    @GetMapping("/all")
    @Operation(summary = "Get all restaurants")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurants retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @ResponseStatus(HttpStatus.OK)
    List<RestaurantResponse> getAllRestaurants();

    @PutMapping("/{id}/update")
    @Operation(summary = "Update a restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurant updated successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @ResponseStatus(HttpStatus.OK)
    RestaurantResponse updateRestaurant(@PathVariable String id, @Validated(OnUpdateGroup.class) @RequestBody RestaurantRequest restaurantRequest);

    @DeleteMapping("/{id}/delete")
    @Operation(summary = "Delete a restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurant deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @ResponseStatus(HttpStatus.OK)
    void deleteRestaurant(@PathVariable String id);

    @PatchMapping("/{id}/disable")
    @Operation(summary = "Disable a restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurant disabled successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @ResponseStatus(HttpStatus.OK)
    void disableRestaurant(@PathVariable String id);
}
