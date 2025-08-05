package com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnCreateGroup;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Requisição para criar/atualizar tipo de cozinha")
public class KitchenTypeRequest {

    @Schema(description = "Nome do tipo de cozinha", example = "Italiana", required = true)
    @NotBlank(message = "Nome é obrigatório", groups = OnCreateGroup.class)
    @Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres", groups = OnCreateGroup.class)
    private String name;

    @Schema(description = "Descrição do tipo de cozinha", example = "Cozinha italiana tradicional com massas e pizzas")
    @Size(max = 200, message = "Descrição deve ter no máximo 200 caracteres", groups = OnCreateGroup.class)
    private String description;
}
