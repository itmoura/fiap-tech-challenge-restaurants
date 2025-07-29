package com.fiap.itmoura.tech_challenge_restaurant.presentation.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype.KitchenTypeDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.usecases.KitchenTypeUseCase;
import com.fiap.itmoura.tech_challenge_restaurant.presentation.contracts.KitchenTypeControllerInterfaces;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/kitchen-types")
@RequiredArgsConstructor
public class KitchenTypeController implements KitchenTypeControllerInterfaces {

    private final KitchenTypeUseCase kitchenTypeService;

    @Override
    public KitchenTypeDTO createKitchenType(KitchenTypeDTO kitchenTypeDTO) {
        return kitchenTypeService.createKitchenType(kitchenTypeDTO);
    }

    @Override
    public KitchenTypeDTO updateKitchenType(String id, KitchenTypeDTO kitchenTypeDTO) {
        return kitchenTypeService.updateKitchenType(id, kitchenTypeDTO);
    }

    @Override
    public void deleteKitchenType(String id) {
        kitchenTypeService.deleteKitchenType(id);
    }

    @Override
    public KitchenTypeDTO getKitchenTypeByIdOrName(String idOrName) {
        return KitchenTypeDTO.fromEntity(kitchenTypeService.getKitchenTypeByIdOrName(idOrName));
    }
}
