package com.fiap.itmoura.tech_challenge_restaurant.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.itmoura.tech_challenge_restaurant.controller.interfaces.KitchenTypeControllerInterfaces;
import com.fiap.itmoura.tech_challenge_restaurant.model.dto.KitchenTypeDTO;
import com.fiap.itmoura.tech_challenge_restaurant.services.KitchenTypeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/kitchen-types")
@RequiredArgsConstructor
public class KitchenTypeController implements KitchenTypeControllerInterfaces {

    private final KitchenTypeService kitchenTypeService;

    @Override
    public KitchenTypeDTO createKitchenType(KitchenTypeDTO kitchenTypeDTO) {
        return kitchenTypeService.createKitchenType(kitchenTypeDTO);
    }
}
