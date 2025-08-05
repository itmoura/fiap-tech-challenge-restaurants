package com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype.KitchenTypeDTO;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.OperationDaysTimeData;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.RestaurantEntity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta básica do restaurante sem menu")
public record RestaurantBasicResponse(
    
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

    @Schema(description = "Data da última atualização")
    LocalDateTime lastUpdate,

    @Schema(description = "Data de criação")
    LocalDateTime createdAt
) {

    public static RestaurantBasicResponse fromEntity(RestaurantEntity restaurant) {
        return new RestaurantBasicResponse(
            restaurant.getId(),
            restaurant.getName(),
            restaurant.getAddress(),
            KitchenTypeDTO.fromEntity(restaurant.getKitchenType()),
            restaurant.getDaysOperation(),
            restaurant.getOwnerId(),
            restaurant.getIsActive(),
            restaurant.getLastUpdate(),
            restaurant.getCreatedAt()
        );
    }
}
