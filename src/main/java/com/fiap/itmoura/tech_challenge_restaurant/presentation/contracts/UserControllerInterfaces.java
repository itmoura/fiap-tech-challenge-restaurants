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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnCreateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnUpdateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.user.UserDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

public interface UserControllerInterfaces {

    @PostMapping("/create")
    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "409", description = "User with this email already exists"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    UserDTO createUser(@Validated(OnCreateGroup.class) @RequestBody UserDTO userDTO);

    @GetMapping
    @Operation(summary = "Get all users or filter by user type")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
    })
    @ResponseStatus(HttpStatus.OK)
    List<UserDTO> getUsers(@RequestParam(required = false) String userTypeId);

    @GetMapping("/{id}")
    @Operation(summary = "Get a user by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
    })
    @ResponseStatus(HttpStatus.OK)
    UserDTO getUserById(@PathVariable String id);

    @PutMapping("/{id}/update")
    @Operation(summary = "Update a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request body"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "409", description = "User with this email already exists"),
    })
    @ResponseStatus(HttpStatus.OK)
    UserDTO updateUser(@PathVariable String id, 
                      @Validated(OnUpdateGroup.class) 
                      @RequestBody UserDTO userDTO);

    @DeleteMapping("/{id}/delete")
    @Operation(summary = "Delete a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable String id);
}
