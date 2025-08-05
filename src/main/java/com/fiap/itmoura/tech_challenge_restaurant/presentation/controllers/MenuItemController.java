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
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuItemRequest;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuItemResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuItemWithContextDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.usecases.MenuItemUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Menu Items", description = "API para gerenciamento de itens do menu")
public class MenuItemController {

    private final MenuItemUseCase menuItemUseCase;

    @PostMapping("/api/restaurants/{restaurantId}/menu/{menuId}/item")
    @Operation(summary = "Criar item do menu", description = "Adiciona um novo item a uma categoria do menu")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Item criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Restaurante ou categoria não encontrada")
    })
    public ResponseEntity<MenuItemResponse> createMenuItem(
            @Parameter(description = "ID do restaurante") @PathVariable UUID restaurantId,
            @Parameter(description = "ID da categoria do menu") @PathVariable UUID menuId,
            @Validated(OnCreateGroup.class) @RequestBody MenuItemRequest request) {
        
        MenuItemResponse response = menuItemUseCase.createMenuItem(restaurantId, menuId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/api/restaurants/{restaurantId}/menu/{menuId}/item/{itemId}")
    @Operation(summary = "Atualizar item do menu", description = "Atualiza um item existente do menu")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Restaurante, categoria ou item não encontrado")
    })
    public ResponseEntity<MenuItemResponse> updateMenuItem(
            @Parameter(description = "ID do restaurante") @PathVariable UUID restaurantId,
            @Parameter(description = "ID da categoria do menu") @PathVariable UUID menuId,
            @Parameter(description = "ID do item") @PathVariable UUID itemId,
            @Validated(OnCreateGroup.class) @RequestBody MenuItemRequest request) {
        
        MenuItemResponse response = menuItemUseCase.updateMenuItem(restaurantId, menuId, itemId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/restaurants/{restaurantId}/menu/{menuId}/item/{itemId}")
    @Operation(summary = "Excluir item do menu", description = "Exclui um item do menu")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Item excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Restaurante, categoria ou item não encontrado")
    })
    public ResponseEntity<Void> deleteMenuItem(
            @Parameter(description = "ID do restaurante") @PathVariable UUID restaurantId,
            @Parameter(description = "ID da categoria do menu") @PathVariable UUID menuId,
            @Parameter(description = "ID do item") @PathVariable UUID itemId) {
        
        menuItemUseCase.deleteMenuItem(restaurantId, menuId, itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/restaurants/menu/item/{itemId}")
    @Operation(summary = "Buscar item do menu por ID", description = "Retorna um item específico do menu com contexto completo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item encontrado"),
        @ApiResponse(responseCode = "404", description = "Item não encontrado")
    })
    public ResponseEntity<MenuItemWithContextDTO> getMenuItemById(
            @Parameter(description = "ID do item do menu") @PathVariable UUID itemId) {
        
        MenuItemWithContextDTO response = menuItemUseCase.getMenuItemById(itemId);
        return ResponseEntity.ok(response);
    }
}
