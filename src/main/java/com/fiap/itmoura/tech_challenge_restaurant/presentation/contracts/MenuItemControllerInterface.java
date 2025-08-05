package com.fiap.itmoura.tech_challenge_restaurant.presentation.contracts;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnCreateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuItemRequest;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuItemResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuItemWithContextDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Menu Items", description = "API para gerenciamento de itens do menu")
public interface MenuItemControllerInterface {

    @Operation(
        summary = "Criar item do menu",
        description = "Adiciona um novo item a uma categoria específica do menu"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Item criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MenuItemResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Restaurante ou categoria não encontrada",
            content = @Content(mediaType = "application/json")
        )
    })
    ResponseEntity<MenuItemResponse> createMenuItem(
        @Parameter(description = "ID do restaurante", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
        @PathVariable UUID restaurantId,
        @Parameter(description = "ID da categoria do menu", required = true, example = "550e8400-e29b-41d4-a716-446655440001")
        @PathVariable UUID menuId,
        @Parameter(description = "Dados do item a ser criado", required = true)
        @Validated(OnCreateGroup.class) @RequestBody MenuItemRequest request
    );

    @Operation(
        summary = "Atualizar item do menu",
        description = "Atualiza um item existente do menu"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Item atualizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MenuItemResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Restaurante, categoria ou item não encontrado",
            content = @Content(mediaType = "application/json")
        )
    })
    ResponseEntity<MenuItemResponse> updateMenuItem(
        @Parameter(description = "ID do restaurante", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
        @PathVariable UUID restaurantId,
        @Parameter(description = "ID da categoria do menu", required = true, example = "550e8400-e29b-41d4-a716-446655440001")
        @PathVariable UUID menuId,
        @Parameter(description = "ID do item", required = true, example = "550e8400-e29b-41d4-a716-446655440002")
        @PathVariable UUID itemId,
        @Parameter(description = "Dados atualizados do item", required = true)
        @Validated(OnCreateGroup.class) @RequestBody MenuItemRequest request
    );

    @Operation(
        summary = "Excluir item do menu",
        description = "Remove um item do menu"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Item excluído com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Restaurante, categoria ou item não encontrado",
            content = @Content(mediaType = "application/json")
        )
    })
    ResponseEntity<Void> deleteMenuItem(
        @Parameter(description = "ID do restaurante", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
        @PathVariable UUID restaurantId,
        @Parameter(description = "ID da categoria do menu", required = true, example = "550e8400-e29b-41d4-a716-446655440001")
        @PathVariable UUID menuId,
        @Parameter(description = "ID do item", required = true, example = "550e8400-e29b-41d4-a716-446655440002")
        @PathVariable UUID itemId
    );

    @Operation(
        summary = "Buscar item do menu por ID",
        description = "Retorna um item específico do menu com contexto completo (categoria e restaurante)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Item encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MenuItemWithContextDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Item não encontrado",
            content = @Content(mediaType = "application/json")
        )
    })
    ResponseEntity<MenuItemWithContextDTO> getMenuItemById(
        @Parameter(description = "ID do item do menu", required = true, example = "550e8400-e29b-41d4-a716-446655440002")
        @PathVariable UUID itemId
    );
}
