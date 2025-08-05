package com.fiap.itmoura.tech_challenge_restaurant.application.models.menu;

import java.math.BigDecimal;
import java.util.UUID;

import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.MenuItemEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Item do menu aninhado na categoria")
public class MenuItemNestedDTO {
    
    @Schema(description = "ID único do item", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID id;
    
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

    public static MenuItemNestedDTO fromEntity(MenuItemEntity entity) {
        return MenuItemNestedDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            .description(entity.getDescription())
            .price(entity.getPrice())
            .onlyForLocalConsumption(entity.getOnlyForLocalConsumption())
            .imagePath(entity.getImagePath())
            .isActive(entity.getIsActive())
            .build();
    }
}
