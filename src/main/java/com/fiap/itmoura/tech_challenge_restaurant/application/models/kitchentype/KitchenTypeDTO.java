package com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype;

import java.util.UUID;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnCreateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.KitchenTypeEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record KitchenTypeDTO(

    @Schema(description = "ID do tipo de cozinha", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID id,

    @Schema(description = "Nome do tipo de cozinha", example = "Brasileira")
    @NotNull(message = "Nome é obrigatório", groups = OnCreateGroup.class)
    String name,

    @Schema(description = "Descrição do tipo de cozinha", example = "Cozinha brasileira")
    String description
) {
    public KitchenTypeEntity toEntity() {
        return KitchenTypeEntity.builder()
            .id(id != null ? id : UUID.randomUUID())
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
