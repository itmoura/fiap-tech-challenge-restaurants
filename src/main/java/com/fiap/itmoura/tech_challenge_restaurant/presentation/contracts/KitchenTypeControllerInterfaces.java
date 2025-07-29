package com.fiap.itmoura.tech_challenge_restaurant.presentation.contracts;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype.KitchenTypeDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype.group.OnCreateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype.group.OnUpdateGroup;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface KitchenTypeControllerInterfaces {

    @PostMapping("/create")
    @Operation(summary = "Create a new kitchen type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Kitchen type created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    KitchenTypeDTO createKitchenType(@Validated(OnCreateGroup.class) @RequestBody KitchenTypeDTO kitchenTypeDTO);

    @PutMapping("/{id}/update")
    @Operation(summary = "Update a kitchen type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Kitchen type updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @ResponseStatus(HttpStatus.OK)
    KitchenTypeDTO updateKitchenType(@PathVariable String id, 
                                    @Validated(OnUpdateGroup.class) 
                                    @RequestBody KitchenTypeDTO kitchenTypeDTO);

    @DeleteMapping("/{id}/delete")
    @Operation(summary = "Delete a kitchen type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Kitchen type deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @ResponseStatus(HttpStatus.OK)
    void deleteKitchenType(@PathVariable String id);

    @GetMapping("/{idOrName}")
    @Operation(summary = "Get a kitchen type by id or name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Kitchen type retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
    })
    @ResponseStatus(HttpStatus.OK)
    KitchenTypeDTO getKitchenTypeByIdOrName(@PathVariable String idOrName);
}
