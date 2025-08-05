package com.fiap.itmoura.tech_challenge_restaurant.presentation.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.menuitem.MenuItemDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.usecases.MenuItemUseCase;
import com.fiap.itmoura.tech_challenge_restaurant.presentation.contracts.MenuItemControllerInterfaces;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController implements MenuItemControllerInterfaces {

    private final MenuItemUseCase menuItemUseCase;

    @Override
    public MenuItemDTO createMenuItem(MenuItemDTO menuItemDTO) {
        return menuItemUseCase.createMenuItem(menuItemDTO);
    }

    @Override
    public List<MenuItemDTO> getMenuItems(String restaurantId, Boolean activeOnly) {
        if (restaurantId != null && !restaurantId.trim().isEmpty()) {
            if (activeOnly) {
                return menuItemUseCase.getActiveMenuItemsByRestaurant(restaurantId);
            }
            return menuItemUseCase.getMenuItemsByRestaurant(restaurantId);
        }
        
        if (activeOnly) {
            return menuItemUseCase.getActiveMenuItems();
        }
        
        return menuItemUseCase.getAllMenuItems();
    }

    @Override
    public MenuItemDTO getMenuItemById(String id) {
        return MenuItemDTO.fromEntity(menuItemUseCase.getMenuItemById(id));
    }

    @Override
    public MenuItemDTO updateMenuItem(String id, MenuItemDTO menuItemDTO) {
        return menuItemUseCase.updateMenuItem(id, menuItemDTO);
    }

    @Override
    public MenuItemDTO toggleMenuItemStatus(String id) {
        return menuItemUseCase.toggleMenuItemStatus(id);
    }

    @Override
    public void deleteMenuItem(String id) {
        menuItemUseCase.deleteMenuItem(id);
    }
}
