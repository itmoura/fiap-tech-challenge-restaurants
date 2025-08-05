package com.fiap.itmoura.tech_challenge_restaurant.presentation.contracts;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantBasicResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantFullResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Restaurants", description = "API para gerenciamento de restaurantes")
public interface RestaurantControllerInterface {

    @Operation(
        summary = "Criar restaurante",
        description = "Cria um novo restaurante no sistema com informações básicas e menu opcional"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Restaurante criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RestaurantFullResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Tipo de cozinha não encontrado",
            content = @Content(mediaType = "application/json")
        )
    })
    ResponseEntity<RestaurantFullResponse> createRestaurant(
        @Parameter(description = "Dados do restaurante a ser criado", required = true)
        @Valid @RequestBody RestaurantRequest restaurantRequest
    );

    @Operation(
        summary = "Listar restaurantes básicos",
        description = "Retorna todos os restaurantes com informações básicas (sem menu)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de restaurantes retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RestaurantBasicResponse.class)
            )
        )
    })
    ResponseEntity<List<RestaurantBasicResponse>> getAllRestaurants();

    @Operation(
        summary = "Listar restaurantes completos",
        description = "Retorna todos os restaurantes com informações completas incluindo menu"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de restaurantes completos retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RestaurantFullResponse.class)
            )
        )
    })
    ResponseEntity<List<RestaurantFullResponse>> getAllRestaurantsWithMenu();

    @Operation(
        summary = "Buscar restaurante por ID",
        description = "Retorna um restaurante específico com todas as informações incluindo menu"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Restaurante encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RestaurantFullResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Restaurante não encontrado",
            content = @Content(mediaType = "application/json")
        )
    })
    ResponseEntity<RestaurantFullResponse> getRestaurantById(
        @Parameter(description = "ID do restaurante", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
        @PathVariable String id
    );

    @Operation(
        summary = "Atualizar restaurante",
        description = "Atualiza as informações de um restaurante existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Restaurante atualizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RestaurantFullResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Restaurante não encontrado",
            content = @Content(mediaType = "application/json")
        )
    })
    ResponseEntity<RestaurantFullResponse> updateRestaurant(
        @Parameter(description = "ID do restaurante", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
        @PathVariable String id,
        @Parameter(description = "Dados atualizados do restaurante", required = true)
        @Valid @RequestBody RestaurantRequest restaurantRequest
    );

    @Operation(
        summary = "Excluir restaurante",
        description = "Remove um restaurante do sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Restaurante excluído com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Restaurante não encontrado",
            content = @Content(mediaType = "application/json")
        )
    })
    ResponseEntity<Void> deleteRestaurant(
        @Parameter(description = "ID do restaurante", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
        @PathVariable String id
    );
}
