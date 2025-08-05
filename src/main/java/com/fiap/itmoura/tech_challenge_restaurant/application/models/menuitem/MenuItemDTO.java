package com.fiap.itmoura.tech_challenge_restaurant.application.models.menuitem;

import java.math.BigDecimal;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnCreateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.MenuItemEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record MenuItemDTO(

    @Schema(title = "ID", description = "ID do item do cardápio", example = "123e4567-e89b-12d3-a456-426614174000")
    String id,

    @Schema(title = "Nome", description = "Nome do item do cardápio", example = "Hambúrguer Artesanal")
    @NotNull(message = "Nome é obrigatório", groups = OnCreateGroup.class)
    String name,

    @Schema(title = "Descrição", description = "Descrição do item do cardápio", example = "Hambúrguer com carne artesanal, queijo, bacon e molho especial")
    String description,

    @Schema(title = "Preço", description = "Preço do item do cardápio", example = "25.90")
    @NotNull(message = "Preço é obrigatório", groups = OnCreateGroup.class)
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero", groups = OnCreateGroup.class)
    BigDecimal price,

    @Schema(title = "Apenas para consumo local", description = "Se o item está disponível apenas para consumo no restaurante", example = "false")
    Boolean onlyForLocalConsumption,

    @Schema(title = "Caminho da imagem", description = "Caminho onde a imagem do prato está armazenada", example = "/images/hamburguer-artesanal.jpg")
    String imagePath,

    @Schema(title = "ID do Restaurante", description = "ID do restaurante ao qual o item pertence", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "ID do restaurante é obrigatório", groups = OnCreateGroup.class)
    String restaurantId,

    @Schema(title = "Ativo", description = "Se o item está ativo no cardápio", example = "true")
    Boolean isActive
) {
    public MenuItemEntity toEntity() {
        return MenuItemEntity.builder()
            .name(name)
            .description(description)
            .price(price)
            .onlyForLocalConsumption(onlyForLocalConsumption != null ? onlyForLocalConsumption : false)
            .imagePath(imagePath)
            .restaurantId(restaurantId)
            .isActive(isActive != null ? isActive : true)
            .build();
    }

    public static MenuItemDTO fromEntity(MenuItemEntity menuItem) {
        return new MenuItemDTO(
            menuItem.getId(),
            menuItem.getName(),
            menuItem.getDescription(),
            menuItem.getPrice(),
            menuItem.getOnlyForLocalConsumption(),
            menuItem.getImagePath(),
            menuItem.getRestaurantId(),
            menuItem.getIsActive()
        );
    }
}
