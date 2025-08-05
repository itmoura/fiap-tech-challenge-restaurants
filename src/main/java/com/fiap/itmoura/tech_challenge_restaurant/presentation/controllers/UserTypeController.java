package com.fiap.itmoura.tech_challenge_restaurant.presentation.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.usertype.UserTypeDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.usecases.UserTypeUseCase;
import com.fiap.itmoura.tech_challenge_restaurant.presentation.contracts.UserTypeControllerInterfaces;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user-types")
@RequiredArgsConstructor
public class UserTypeController implements UserTypeControllerInterfaces {

    private final UserTypeUseCase userTypeUseCase;

    @Override
    public UserTypeDTO createUserType(UserTypeDTO userTypeDTO) {
        return userTypeUseCase.createUserType(userTypeDTO);
    }

    @Override
    public List<UserTypeDTO> getAllUserTypes() {
        return userTypeUseCase.getAllUserTypes();
    }

    @Override
    public UserTypeDTO getUserTypeByIdOrName(String idOrName) {
        return UserTypeDTO.fromEntity(userTypeUseCase.getUserTypeByIdOrName(idOrName));
    }

    @Override
    public UserTypeDTO updateUserType(String id, UserTypeDTO userTypeDTO) {
        return userTypeUseCase.updateUserType(id, userTypeDTO);
    }

    @Override
    public void deleteUserType(String id) {
        userTypeUseCase.deleteUserType(id);
    }
}
