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
import com.fiap.itmoura.tech_challenge_restaurant.presentation.contracts.MenuControllerInterface;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/menu")
@RequiredArgsConstructor
public class MenuController implements MenuControllerInterface {

    private final MenuUseCase menuUseCase;

    @Override
    @PostMapping
    public ResponseEntity<MenuCategoryResponse> createMenuCategory(
            @PathVariable String restaurantId,
            @Validated(OnCreateGroup.class) @RequestBody MenuCategoryRequest request) {
        
        MenuCategoryResponse response = menuUseCase.createMenuCategory(restaurantId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @PutMapping("/{menuId}")
    public ResponseEntity<MenuCategoryResponse> updateMenuCategory(
            @PathVariable String restaurantId,
            @PathVariable String menuId,
            @Validated(OnCreateGroup.class) @RequestBody MenuCategoryRequest request) {
        
        MenuCategoryResponse response = menuUseCase.updateMenuCategory(restaurantId, menuId, request);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenuCategory(
            @PathVariable String restaurantId,
            @PathVariable String menuId) {
        
        menuUseCase.deleteMenuCategory(restaurantId, menuId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/{menuId}")
    public ResponseEntity<MenuCategoryResponse> getMenuCategory(
            @PathVariable String restaurantId,
            @PathVariable String menuId) {
        
        MenuCategoryResponse response = menuUseCase.getMenuCategory(restaurantId, menuId);
        return ResponseEntity.ok(response);
    }
}
