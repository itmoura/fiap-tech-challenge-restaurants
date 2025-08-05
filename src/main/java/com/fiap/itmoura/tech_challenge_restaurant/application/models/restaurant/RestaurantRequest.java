package com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant;

import java.util.List;
import java.util.UUID;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnCreateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype.KitchenTypeDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuCategoryDTO;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.OperationDaysTimeData;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record RestaurantRequest(
    
    @Schema(description = "Nome do restaurante", example = "Restaurante do João")
    @NotNull(message = "Nome é obrigatório", groups = OnCreateGroup.class)
    String name,
    
    @Schema(description = "Endereço do restaurante", example = "Rua das Flores, 123")
    @NotNull(message = "Endereço é obrigatório", groups = OnCreateGroup.class)
    String address,
    
    @Schema(description = "Tipo de cozinha do restaurante")
    @NotNull(message = "Tipo de cozinha é obrigatório", groups = OnCreateGroup.class)
    KitchenTypeDTO kitchenType,
    
    @Schema(description = "Horários de funcionamento do restaurante")
    @NotNull(message = "Horários de funcionamento são obrigatórios", groups = OnCreateGroup.class)
    List<OperationDaysTimeData> daysOperation,
    
    @Schema(description = "ID do dono do restaurante", example = "550e8400-e29b-41d4-a716-446655440001")
    @NotNull(message = "ID do dono é obrigatório", groups = OnCreateGroup.class)
    String ownerId,

    @Schema(description = "Se o restaurante está ativo", example = "true")
    Boolean isActive,

    @Schema(description = "Menu do restaurante")
    List<MenuCategoryDTO> menu
) {
}
