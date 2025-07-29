package com.fiap.itmoura.tech_challenge_restaurant.domain.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuRestaurantData {

    @Schema(title = "Nome", description = "Nome do item do menu", example = "Hambúrguer")
    @NotNull(message = "Nome é obrigatório")
    private String name;

    @Schema(title = "Descrição", description = "Descrição do item do menu", example = "Hambúrguer com queijo, bacon e alface")
    private String description;

    @Schema(title = "Preço", description = "Preço do item do menu", example = "10.00")
    @NotNull(message = "Preço é obrigatório")
    private Double price;

    @Schema(title = "URL da imagem", description = "URL da imagem do item do menu", example = "https://example.com/image.jpg")
    private String imageUrl;

    @Schema(title = "Disponível apenas para consumo no local", description = "Se o item do menu é disponível apenas para consumo no local, valor padrão é false", example = "false")
    private Boolean onlyForLocalConsumption;

    @Schema(title = "ID do restaurante", description = "ID do restaurante que o item do menu pertence", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "ID do restaurante é obrigatório")
    private String restaurantId;
    
}
