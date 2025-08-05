package com.fiap.itmoura.tech_challenge_restaurant.application.models.user;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnCreateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.UserEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserDTO(

    @Schema(title = "ID", description = "ID do usuário", example = "123e4567-e89b-12d3-a456-426614174000")
    String id,

    @Schema(title = "Nome", description = "Nome do usuário", example = "João Silva")
    @NotNull(message = "Nome é obrigatório", groups = OnCreateGroup.class)
    String name,

    @Schema(title = "Email", description = "Email do usuário", example = "joao@email.com")
    @NotNull(message = "Email é obrigatório", groups = OnCreateGroup.class)
    @Email(message = "Email deve ter um formato válido", groups = OnCreateGroup.class)
    String email,

    @Schema(title = "Tipo de Usuário ID", description = "ID do tipo de usuário", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "Tipo de usuário é obrigatório", groups = OnCreateGroup.class)
    String userTypeId
) {
    public UserEntity toEntity() {
        return UserEntity.builder()
            .name(name)
            .email(email)
            .userTypeId(userTypeId)
            .build();
    }

    public static UserDTO fromEntity(UserEntity user) {
        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getUserTypeId()
        );
    }
}
