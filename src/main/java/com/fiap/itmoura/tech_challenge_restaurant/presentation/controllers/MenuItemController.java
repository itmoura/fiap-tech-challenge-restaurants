package com.fiap.itmoura.tech_challenge_restaurant.presentation.controllers;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnCreateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuItemRequest;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuItemResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuItemWithContextDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.usecases.MenuItemUseCase;
import com.fiap.itmoura.tech_challenge_restaurant.presentation.contracts.MenuItemControllerInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MenuItemController implements MenuItemControllerInterface {

    private final MenuItemUseCase menuItemUseCase;

    @Override
    @PostMapping("/api/restaurants/{restaurantId}/menu/{menuId}/item")
    public ResponseEntity<MenuItemResponse> createMenuItem(
            @PathVariable String restaurantId,
            @PathVariable String menuId,
            @Validated(OnCreateGroup.class) @RequestBody MenuItemRequest request) {
        
        MenuItemResponse response = menuItemUseCase.createMenuItem(restaurantId, menuId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PutMapping("/api/restaurants/{restaurantId}/menu/{menuId}/item/{itemId}")
    public ResponseEntity<MenuItemResponse> updateMenuItem(
            @PathVariable String restaurantId,
            @PathVariable String menuId,
            @PathVariable String itemId,
            @Validated(OnCreateGroup.class) @RequestBody MenuItemRequest request) {
        
        MenuItemResponse response = menuItemUseCase.updateMenuItem(restaurantId, menuId, itemId, request);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/api/restaurants/{restaurantId}/menu/{menuId}/item/{itemId}")
    public ResponseEntity<Void> deleteMenuItem(
            @PathVariable String restaurantId,
            @PathVariable String menuId,
            @PathVariable String itemId) {
        
        menuItemUseCase.deleteMenuItem(restaurantId, menuId, itemId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/api/restaurants/menu/item/{itemId}")
    public ResponseEntity<MenuItemWithContextDTO> getMenuItemById(@PathVariable String itemId) {
        MenuItemWithContextDTO response = menuItemUseCase.getMenuItemById(itemId);
        return ResponseEntity.ok(response);
    }
}
