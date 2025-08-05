package com.fiap.itmoura.tech_challenge_restaurant.presentation.contracts;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnCreateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuCategoryRequest;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuCategoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Menu Categories", description = "API para gerenciamento de categorias do menu")
public interface MenuControllerInterface {

    @Operation(
        summary = "Criar categoria de menu",
        description = "Cria uma nova categoria de menu para um restaurante específico"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Categoria criada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MenuCategoryResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Restaurante não encontrado",
            content = @Content(mediaType = "application/json")
        )
    })
    ResponseEntity<MenuCategoryResponse> createMenuCategory(
        @Parameter(description = "ID do restaurante", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
        @PathVariable String restaurantId,
        @Parameter(description = "Dados da categoria a ser criada", required = true)
        @Validated(OnCreateGroup.class) @RequestBody MenuCategoryRequest request
    );

    @Operation(
        summary = "Atualizar categoria de menu",
        description = "Atualiza uma categoria de menu existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Categoria atualizada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MenuCategoryResponse.class)
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
    ResponseEntity<MenuCategoryResponse> updateMenuCategory(
        @Parameter(description = "ID do restaurante", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
        @PathVariable String restaurantId,
        @Parameter(description = "ID da categoria do menu", required = true, example = "550e8400-e29b-41d4-a716-446655440001")
        @PathVariable String menuId,
        @Parameter(description = "Dados atualizados da categoria", required = true)
        @Validated(OnCreateGroup.class) @RequestBody MenuCategoryRequest request
    );

    @Operation(
        summary = "Excluir categoria de menu",
        description = "Remove uma categoria de menu do restaurante"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Categoria excluída com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Restaurante ou categoria não encontrada",
            content = @Content(mediaType = "application/json")
        )
    })
    ResponseEntity<Void> deleteMenuCategory(
        @Parameter(description = "ID do restaurante", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
        @PathVariable String restaurantId,
        @Parameter(description = "ID da categoria do menu", required = true, example = "550e8400-e29b-41d4-a716-446655440001")
        @PathVariable String menuId
    );

    @Operation(
        summary = "Buscar categoria de menu",
        description = "Retorna uma categoria de menu específica com todos os seus itens"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Categoria encontrada",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = MenuCategoryResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Restaurante ou categoria não encontrada",
            content = @Content(mediaType = "application/json")
        )
    })
    ResponseEntity<MenuCategoryResponse> getMenuCategory(
        @Parameter(description = "ID do restaurante", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
        @PathVariable String restaurantId,
        @Parameter(description = "ID da categoria do menu", required = true, example = "550e8400-e29b-41d4-a716-446655440001")
        @PathVariable String menuId
    );
}
