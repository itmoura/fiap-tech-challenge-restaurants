package com.fiap.itmoura.tech_challenge_restaurant.application.models.menu;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnCreateGroup;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Requisição para criar/atualizar categoria do menu")
public class MenuCategoryRequest {
    
    @Schema(description = "Tipo da categoria", example = "Lanche", required = true)
    @NotBlank(message = "Tipo da categoria é obrigatório", groups = OnCreateGroup.class)
    private String type;
}
