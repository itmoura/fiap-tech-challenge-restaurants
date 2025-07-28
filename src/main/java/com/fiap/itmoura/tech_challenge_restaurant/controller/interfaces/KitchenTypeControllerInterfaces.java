package com.fiap.itmoura.tech_challenge_restaurant.controller.interfaces;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fiap.itmoura.tech_challenge_restaurant.model.dto.OnCreate;
import com.fiap.itmoura.tech_challenge_restaurant.model.dto.KitchenTypeDTO;

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
    KitchenTypeDTO createKitchenType(@Validated(OnCreate.class) @RequestBody KitchenTypeDTO kitchenTypeDTO);
}
