package com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.KitchenTypeDocumentEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.KitchenTypeEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Resposta do tipo de cozinha")
public class KitchenTypeResponse {

    @Schema(description = "ID único do tipo de cozinha", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @Schema(description = "Nome do tipo de cozinha", example = "Italiana")
    private String name;

    @Schema(description = "Descrição do tipo de cozinha", example = "Cozinha italiana tradicional com massas e pizzas")
    private String description;

    @Schema(description = "Data de criação")
    private LocalDateTime createdAt;

    @Schema(description = "Data da última atualização")
    private LocalDateTime lastUpdate;

    public static KitchenTypeResponse fromEntity(KitchenTypeEntity entity) {
        return KitchenTypeResponse.builder()
            .id(entity.getId())
            .name(entity.getName())
            .description(entity.getDescription())
            .createdAt(entity.getCreatedAt())
            .lastUpdate(entity.getLastUpdate())
            .build();
    }

    public static KitchenTypeResponse fromEntity(KitchenTypeDocumentEntity entity) {
        return KitchenTypeResponse.builder()
            .id(entity.getId())
            .name(entity.getName())
            .description(entity.getDescription())
            .createdAt(entity.getCreatedAt())
            .lastUpdate(entity.getLastUpdate())
            .build();
    }
}
