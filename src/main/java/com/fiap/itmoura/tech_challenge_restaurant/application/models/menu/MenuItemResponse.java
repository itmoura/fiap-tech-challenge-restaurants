package com.fiap.itmoura.tech_challenge_restaurant.application.models.menu;

import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.MenuItemEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Resposta do item do menu")
public class MenuItemResponse {
    
    @Schema(description = "ID único do item", example = "550e8400-e29b-41d4-a716-446655440001")
    private String id;
    
    @Schema(description = "Nome do item", example = "Hambúrguer Artesanal")
    private String name;
    
    @Schema(description = "Descrição do item", example = "Hambúrguer com carne artesanal, queijo, bacon e molho especial")
    private String description;
    
    @Schema(description = "Preço do item", example = "25.90")
    private BigDecimal price;
    
    @Schema(description = "Disponível apenas para consumo local", example = "false")
    private Boolean onlyForLocalConsumption;
    
    @Schema(description = "Caminho da imagem", example = "/images/hamburguer-artesanal.jpg")
    private String imagePath;
    
    @Schema(description = "Item ativo", example = "true")
    private Boolean isActive;
    
    @Schema(description = "ID do restaurante", example = "550e8400-e29b-41d4-a716-446655440002")
    private String restaurantId;
    
    @Schema(description = "ID da categoria", example = "550e8400-e29b-41d4-a716-446655440003")
    private String categoryId;

    public static MenuItemResponse fromEntity(MenuItemEntity entity, String restaurantId, String categoryId) {
        return MenuItemResponse.builder()
            .id(entity.getId())
            .name(entity.getName())
            .description(entity.getDescription())
            .price(entity.getPrice())
            .onlyForLocalConsumption(entity.getOnlyForLocalConsumption())
            .imagePath(entity.getImagePath())
            .isActive(entity.getIsActive())
            .restaurantId(restaurantId)
            .categoryId(categoryId)
            .build();
    }
}
