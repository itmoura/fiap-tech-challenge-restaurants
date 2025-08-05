package com.fiap.itmoura.tech_challenge_restaurant.application.models.menu;

import java.math.BigDecimal;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnCreateGroup;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Requisição para criar/atualizar item do menu")
public class MenuItemRequest {
    
    @Schema(description = "Nome do item", example = "Hambúrguer Artesanal", required = true)
    @NotBlank(message = "Nome do item é obrigatório", groups = OnCreateGroup.class)
    private String name;
    
    @Schema(description = "Descrição do item", example = "Hambúrguer com carne artesanal, queijo, bacon e molho especial")
    private String description;
    
    @Schema(description = "Preço do item", example = "25.90", required = true)
    @NotNull(message = "Preço é obrigatório", groups = OnCreateGroup.class)
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero", groups = OnCreateGroup.class)
    private BigDecimal price;
    
    @Schema(description = "Disponível apenas para consumo local", example = "false")
    private Boolean onlyForLocalConsumption;
    
    @Schema(description = "Caminho da imagem", example = "/images/hamburguer-artesanal.jpg")
    private String imagePath;
    
    @Schema(description = "Item ativo", example = "true")
    private Boolean isActive;
}
