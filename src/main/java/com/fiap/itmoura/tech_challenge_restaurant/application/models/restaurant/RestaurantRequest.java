package com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant;

import java.util.List;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnCreateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.OperationDaysTimeData;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record RestaurantRequest(
    
    @Schema(title = "Nome", description = "Nome do restaurante", example = "Restaurante do João")
    @NotNull(message = "Nome é obrigatório", groups = OnCreateGroup.class)
    String name,
    
    @Schema(title = "Tipo de cozinha", description = "Tipo de cozinha do restaurante (ID ou nome)", example = "6886d82adf214492b03510f8 ou Japonesa")
    @NotNull(message = "Tipo de cozinha é obrigatório", groups = OnCreateGroup.class)
    String kitchenType,
    
    @Schema(title = "Endereço", description = "Endereço do restaurante", example = "Rua das Flores, 123")
    @NotNull(message = "Endereço é obrigatório", groups = OnCreateGroup.class)
    String address,
    
    @Schema(title = "Horários de funcionamento", description = "Horários de funcionamento do restaurante", example = "[{\"day\": \"Segunda-feira\", \"openingHours\": \"08:00\", \"closingHours\": \"18:00\"}]")
    @NotNull(message = "Horários de funcionamento são obrigatórios", groups = OnCreateGroup.class)
    List<OperationDaysTimeData> daysOperation,
    
    @Schema(title = "ID do dono", description = "ID do dono do restaurante", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "ID do dono é obrigatório", groups = OnCreateGroup.class)
    String ownerId,

    @Schema(title = "Ativo", description = "Se o restaurante está ativo, por padrão é true", example = "true")
    Boolean isActive,

    @Schema(title = "Avaliação", description = "Avaliação do restaurante", example = "4.5")
    Double rating
) {
}
