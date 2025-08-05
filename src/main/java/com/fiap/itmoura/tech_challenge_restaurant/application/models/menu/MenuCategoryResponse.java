package com.fiap.itmoura.tech_challenge_restaurant.application.models.menu;

import java.util.List;
import java.util.UUID;

import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.MenuCategoryEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Resposta da categoria do menu")
public class MenuCategoryResponse {
    
    @Schema(description = "ID único da categoria", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;
    
    @Schema(description = "Tipo da categoria", example = "Lanche")
    private String type;
    
    @Schema(description = "ID do restaurante", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID restaurantId;
    
    @Schema(description = "Lista de itens da categoria")
    private List<MenuItemNestedDTO> items;

    public static MenuCategoryResponse fromEntity(MenuCategoryEntity entity, UUID restaurantId) {
        List<MenuItemNestedDTO> items = entity.getItems() != null 
            ? entity.getItems().stream()
                .map(MenuItemNestedDTO::fromEntity)
                .toList()
            : List.of();

        return MenuCategoryResponse.builder()
            .id(entity.getId())
            .type(entity.getType())
            .restaurantId(restaurantId)
            .items(items)
            .build();
    }
}
