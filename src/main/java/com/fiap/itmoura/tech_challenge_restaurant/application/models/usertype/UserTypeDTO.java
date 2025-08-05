package com.fiap.itmoura.tech_challenge_restaurant.application.models.usertype;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnCreateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.UserTypeEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UserTypeDTO(

    @Schema(title = "ID", description = "ID do tipo de usuário", example = "123e4567-e89b-12d3-a456-426614174000")
    String id,

    @Schema(title = "Nome", description = "Nome do tipo de usuário", example = "Dono de Restaurante")
    @NotNull(message = "Nome é obrigatório", groups = OnCreateGroup.class)
    String name
) {
    public UserTypeEntity toEntity() {
        return UserTypeEntity.builder()
            .name(name)
            .build();
    }

    public static UserTypeDTO fromEntity(UserTypeEntity userType) {
        return new UserTypeDTO(
            userType.getId(),
            userType.getName()
        );
    }
}
