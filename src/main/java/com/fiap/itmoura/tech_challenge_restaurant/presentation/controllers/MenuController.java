package com.fiap.itmoura.tech_challenge_restaurant.presentation.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnCreateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuCategoryRequest;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuCategoryResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.usecases.MenuUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/menu")
@RequiredArgsConstructor
@Tag(name = "Menu Categories", description = "API para gerenciamento de categorias do menu")
public class MenuController {

    private final MenuUseCase menuUseCase;

    @PostMapping
    @Operation(summary = "Criar categoria de menu", description = "Cria uma nova categoria de menu para o restaurante")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado")
    })
    public ResponseEntity<MenuCategoryResponse> createMenuCategory(
            @Parameter(description = "ID do restaurante") @PathVariable UUID restaurantId,
            @Validated(OnCreateGroup.class) @RequestBody MenuCategoryRequest request) {
        
        MenuCategoryResponse response = menuUseCase.createMenuCategory(restaurantId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{menuId}")
    @Operation(summary = "Atualizar categoria de menu", description = "Atualiza uma categoria de menu existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Restaurante ou categoria não encontrada")
    })
    public ResponseEntity<MenuCategoryResponse> updateMenuCategory(
            @Parameter(description = "ID do restaurante") @PathVariable UUID restaurantId,
            @Parameter(description = "ID da categoria do menu") @PathVariable UUID menuId,
            @Validated(OnCreateGroup.class) @RequestBody MenuCategoryRequest request) {
        
        MenuCategoryResponse response = menuUseCase.updateMenuCategory(restaurantId, menuId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{menuId}")
    @Operation(summary = "Excluir categoria de menu", description = "Exclui uma categoria de menu")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante ou categoria não encontrada")
    })
    public ResponseEntity<Void> deleteMenuCategory(
            @Parameter(description = "ID do restaurante") @PathVariable UUID restaurantId,
            @Parameter(description = "ID da categoria do menu") @PathVariable UUID menuId) {
        
        menuUseCase.deleteMenuCategory(restaurantId, menuId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{menuId}")
    @Operation(summary = "Buscar categoria de menu", description = "Busca uma categoria de menu específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoria encontrada"),
        @ApiResponse(responseCode = "404", description = "Restaurante ou categoria não encontrada")
    })
    public ResponseEntity<MenuCategoryResponse> getMenuCategory(
            @Parameter(description = "ID do restaurante") @PathVariable UUID restaurantId,
            @Parameter(description = "ID da categoria do menu") @PathVariable UUID menuId) {
        
        MenuCategoryResponse response = menuUseCase.getMenuCategory(restaurantId, menuId);
        return ResponseEntity.ok(response);
    }
}
