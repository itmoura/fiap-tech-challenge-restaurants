package com.fiap.itmoura.tech_challenge_restaurant.presentation.controllers;

import java.util.List;
import java.util.UUID;

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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurants", description = "API para gerenciamento de restaurantes")
public class RestaurantController {

    private final RestaurantUseCase restaurantService;

    @PostMapping
    @Operation(summary = "Criar restaurante", description = "Cria um novo restaurante com menu")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Restaurante criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<RestaurantFullResponse> createRestaurant(
            @Valid @RequestBody RestaurantRequest restaurantRequest) {
        RestaurantFullResponse response = restaurantService.createRestaurant(restaurantRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar restaurantes", description = "Retorna todos os restaurantes sem menu")
    @ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada com sucesso")
    public ResponseEntity<List<RestaurantBasicResponse>> getAllRestaurants() {
        List<RestaurantBasicResponse> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/full")
    @Operation(summary = "Listar restaurantes completos", description = "Retorna todos os restaurantes com menu completo")
    @ApiResponse(responseCode = "200", description = "Lista de restaurantes completos retornada com sucesso")
    public ResponseEntity<List<RestaurantFullResponse>> getAllRestaurantsWithMenu() {
        List<RestaurantFullResponse> restaurants = restaurantService.getAllRestaurantsWithMenu();
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar restaurante por ID", description = "Retorna um restaurante específico com menu completo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<RestaurantFullResponse> getRestaurantById(
            @Parameter(description = "ID do restaurante") @PathVariable UUID id) {
        RestaurantFullResponse restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(restaurant);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar restaurante", description = "Atualiza um restaurante existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<RestaurantFullResponse> updateRestaurant(
            @Parameter(description = "ID do restaurante") @PathVariable UUID id,
            @Valid @RequestBody RestaurantRequest restaurantRequest) {
        RestaurantFullResponse response = restaurantService.updateRestaurant(id, restaurantRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir restaurante", description = "Exclui um restaurante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Restaurante excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<Void> deleteRestaurant(
            @Parameter(description = "ID do restaurante") @PathVariable UUID id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}
