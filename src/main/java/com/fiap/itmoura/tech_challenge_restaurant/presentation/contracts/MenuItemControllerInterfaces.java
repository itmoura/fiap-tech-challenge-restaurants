package com.fiap.itmoura.tech_challenge_restaurant.presentation.contracts;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnCreateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnUpdateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menuitem.MenuItemDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface MenuItemControllerInterfaces {

    @PostMapping("/create")
    @Operation(summary = "Create a new menu item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Menu item created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "404", description = "Restaurant not found"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    MenuItemDTO createMenuItem(@Validated(OnCreateGroup.class) @RequestBody MenuItemDTO menuItemDTO);

    @GetMapping
    @Operation(summary = "Get menu items with optional filters")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Menu items retrieved successfully"),
    })
    @ResponseStatus(HttpStatus.OK)
    List<MenuItemDTO> getMenuItems(@RequestParam(required = false) String restaurantId,
                                  @RequestParam(required = false, defaultValue = "false") Boolean activeOnly);

    @GetMapping("/{id}")
    @Operation(summary = "Get a menu item by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Menu item retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Menu item not found"),
    })
    @ResponseStatus(HttpStatus.OK)
    MenuItemDTO getMenuItemById(@PathVariable String id);

    @PutMapping("/{id}/update")
    @Operation(summary = "Update a menu item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Menu item updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "404", description = "Menu item or restaurant not found"),
    })
    @ResponseStatus(HttpStatus.OK)
    MenuItemDTO updateMenuItem(@PathVariable String id, 
                              @Validated(OnUpdateGroup.class) 
                              @RequestBody MenuItemDTO menuItemDTO);

    @PatchMapping("/{id}/toggle-status")
    @Operation(summary = "Toggle menu item active status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Menu item status toggled successfully"),
        @ApiResponse(responseCode = "404", description = "Menu item not found"),
    })
    @ResponseStatus(HttpStatus.OK)
    MenuItemDTO toggleMenuItemStatus(@PathVariable String id);

    @DeleteMapping("/{id}/delete")
    @Operation(summary = "Delete a menu item")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Menu item deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Menu item not found"),
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteMenuItem(@PathVariable String id);
}
