package com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnCreateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.KitchenTypeEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record KitchenTypeDTO(

    @Schema(title = "ID", description = "ID do tipo de cozinha", example = "123e4567-e89b-12d3-a456-426614174000")
    String id,

    @Schema(title = "Nome", description = "Nome do tipo de cozinha", example = "Brasileira")
    @NotNull(message = "Nome é obrigatório", groups = OnCreateGroup.class)
    String name,

    @Schema(title = "Descrição", description = "Descrição do tipo de cozinha", example = "Cozinha brasileira")
    String description
) {
    public KitchenTypeEntity toEntity() {
        return KitchenTypeEntity.builder()
            .name(name)
            .description(description)
            .build();
    }

    public static KitchenTypeDTO fromEntity(KitchenTypeEntity kitchenType) {
        return new KitchenTypeDTO(
            kitchenType.getId(),
            kitchenType.getName(), 
            kitchenType.getDescription()
        );
    }
}
