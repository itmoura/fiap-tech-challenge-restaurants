package com.fiap.itmoura.tech_challenge_restaurant.model.response;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

import com.fiap.itmoura.tech_challenge_restaurant.model.data.OperationDaysTimeData;
import com.fiap.itmoura.tech_challenge_restaurant.model.dto.KitchenTypeDTO;
import com.fiap.itmoura.tech_challenge_restaurant.model.entity.RestaurantEntity;

public record RestaurantResponse(
    
    @Schema(title = "ID", description = "ID do restaurante", example = "123e4567-e89b-12d3-a456-426614174000")
    String id,

    @Schema(title = "Nome", description = "Nome do restaurante", example = "Restaurante do João")
    String name,

    @Schema(title = "Tipo de cozinha", description = "Tipo de cozinha do restaurante", example = "Japonesa")
    KitchenTypeDTO kitchenType,

    @Schema(title = "Endereço", description = "Endereço do restaurante", example = "Rua das Flores, 123")
    String address,

    @Schema(title = "Horários de funcionamento", description = "Horários de funcionamento do restaurante", example = "[{\"day\": \"Segunda-feira\", \"openingHours\": \"08:00\", \"closingHours\": \"18:00\"}]")
    List<OperationDaysTimeData> daysOperation,

    @Schema(title = "ID do dono", description = "ID do dono do restaurante", example = "123e4567-e89b-12d3-a456-426614174000")
    String ownerId,

    @Schema(title = "Ativo", description = "Se o restaurante está ativo, por padrão é true", example = "true")
    Boolean isActive,

    @Schema(title = "Avaliação", description = "Avaliação do restaurante", example = "4.5")
    Double rating,

    @Schema(title = "lastUpdate", description = "Last update timestamp", example = "2023-10-01T12:00:00Z")
    LocalDateTime lastUpdate,

    @Schema(title = "createdAt", description = "Creation timestamp", example = "2023-10-01T12:00:00Z")
    LocalDateTime createdAt
) {

    public static RestaurantResponse fromEntity(RestaurantEntity restaurant) {
        return new RestaurantResponse(
            restaurant.getId(),
            restaurant.getName(),
            KitchenTypeDTO.fromEntity(restaurant.getKitchenType()),
            restaurant.getAddress(),
            restaurant.getDaysOperation(),
            restaurant.getOwnerId(),
            restaurant.getIsActive(),
            restaurant.getRating(),
            restaurant.getLastUpdate(),
            restaurant.getCreatedAt()
        );
    }
}
