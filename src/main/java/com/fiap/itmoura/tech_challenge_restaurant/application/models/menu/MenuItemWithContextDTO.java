package com.fiap.itmoura.tech_challenge_restaurant.application.models.menu;

import java.math.BigDecimal;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Item do menu com informações de contexto (restaurante e categoria)")
public class MenuItemWithContextDTO {
    
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
    
    @Schema(description = "Categoria do item")
    private MenuCategoryContextDTO category;
    
    @Schema(description = "Restaurante do item")
    private RestaurantContextDTO restaurant;
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "Contexto da categoria")
    public static class MenuCategoryContextDTO {
        @Schema(description = "ID da categoria")
        private UUID id;
        
        @Schema(description = "Tipo da categoria", example = "Lanche")
        private String type;
    }
    
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "Contexto do restaurante")
    public static class RestaurantContextDTO {
        @Schema(description = "ID do restaurante")
        private UUID id;
        
        @Schema(description = "Nome do restaurante", example = "Restaurante do João")
        private String name;
        
        @Schema(description = "Endereço do restaurante", example = "Rua das Flores, 123")
        private String address;
    }
}
