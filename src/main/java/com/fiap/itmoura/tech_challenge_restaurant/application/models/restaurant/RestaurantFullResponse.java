package com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype.KitchenTypeDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuCategoryDTO;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.OperationDaysTimeData;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.RestaurantEntity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta completa do restaurante com menu")
public record RestaurantFullResponse(
    
    @Schema(description = "ID do restaurante", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id,

    @Schema(description = "Nome do restaurante", example = "Restaurante do João")
    String name,

    @Schema(description = "Endereço do restaurante", example = "Rua das Flores, 123")
    String address,

    @Schema(description = "Tipo de cozinha do restaurante")
    KitchenTypeDTO kitchenType,

    @Schema(description = "Horários de funcionamento do restaurante")
    List<OperationDaysTimeData> daysOperation,

    @Schema(description = "ID do dono do restaurante", example = "550e8400-e29b-41d4-a716-446655440001")
    UUID ownerId,

    @Schema(description = "Se o restaurante está ativo", example = "true")
    Boolean isActive,

    @Schema(description = "Menu completo do restaurante")
    List<MenuCategoryDTO> menu,

    @Schema(description = "Data da última atualização")
    LocalDateTime lastUpdate,

    @Schema(description = "Data de criação")
    LocalDateTime createdAt
) {

    public static RestaurantFullResponse fromEntity(RestaurantEntity restaurant) {
        List<MenuCategoryDTO> menuCategories = restaurant.getMenu() != null 
            ? restaurant.getMenu().stream()
                .map(MenuCategoryDTO::fromEntity)
                .toList()
            : List.of();

        return new RestaurantFullResponse(
            restaurant.getId(),
            restaurant.getName(),
            restaurant.getAddress(),
            KitchenTypeDTO.fromEntity(restaurant.getKitchenType()),
            restaurant.getDaysOperation(),
            restaurant.getOwnerId(),
            restaurant.getIsActive(),
            menuCategories,
            restaurant.getLastUpdate(),
            restaurant.getCreatedAt()
        );
    }
}
