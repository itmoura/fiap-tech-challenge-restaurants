package com.fiap.itmoura.tech_challenge_restaurant.presentation.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.user.UserDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.usecases.UserUseCase;
import com.fiap.itmoura.tech_challenge_restaurant.presentation.contracts.UserControllerInterfaces;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserControllerInterfaces {

    private final UserUseCase userUseCase;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        return userUseCase.createUser(userDTO);
    }

    @Override
    public List<UserDTO> getUsers(String userTypeId) {
        if (userTypeId != null && !userTypeId.trim().isEmpty()) {
            return userUseCase.getUsersByType(userTypeId);
        }
        return userUseCase.getAllUsers();
    }

    @Override
    public UserDTO getUserById(String id) {
        return UserDTO.fromEntity(userUseCase.getUserById(id));
    }

    @Override
    public UserDTO updateUser(String id, UserDTO userDTO) {
        return userUseCase.updateUser(id, userDTO);
    }

    @Override
    public void deleteUser(String id) {
        userUseCase.deleteUser(id);
    }
}
