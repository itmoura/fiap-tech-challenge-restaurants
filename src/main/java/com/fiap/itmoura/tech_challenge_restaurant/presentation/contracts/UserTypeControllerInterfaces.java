package com.fiap.itmoura.tech_challenge_restaurant.presentation.contracts;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnCreateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnUpdateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.usertype.UserTypeDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface UserTypeControllerInterfaces {

    @PostMapping("/create")
    @Operation(summary = "Create a new user type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User type created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "409", description = "User type already exists"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    UserTypeDTO createUserType(@Validated(OnCreateGroup.class) @RequestBody UserTypeDTO userTypeDTO);

    @GetMapping
    @Operation(summary = "Get all user types")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User types retrieved successfully"),
    })
    @ResponseStatus(HttpStatus.OK)
    List<UserTypeDTO> getAllUserTypes();

    @GetMapping("/{idOrName}")
    @Operation(summary = "Get a user type by id or name")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User type retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User type not found"),
    })
    @ResponseStatus(HttpStatus.OK)
    UserTypeDTO getUserTypeByIdOrName(@PathVariable String idOrName);

    @PutMapping("/{id}/update")
    @Operation(summary = "Update a user type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User type updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "404", description = "User type not found"),
        @ApiResponse(responseCode = "409", description = "User type name already exists"),
    })
    @ResponseStatus(HttpStatus.OK)
    UserTypeDTO updateUserType(@PathVariable String id, 
                              @Validated(OnUpdateGroup.class) 
                              @RequestBody UserTypeDTO userTypeDTO);

    @DeleteMapping("/{id}/delete")
    @Operation(summary = "Delete a user type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User type deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User type not found"),
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUserType(@PathVariable String id);
}
