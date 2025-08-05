package com.fiap.itmoura.tech_challenge_restaurant.presentation.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnCreateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype.KitchenTypeRequest;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype.KitchenTypeResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.usecases.KitchenTypeUseCase;
import com.fiap.itmoura.tech_challenge_restaurant.presentation.contracts.KitchenTypeControllerInterface;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/kitchen-types")
@RequiredArgsConstructor
public class KitchenTypeController implements KitchenTypeControllerInterface {

    private final KitchenTypeUseCase kitchenTypeUseCase;

    @Override
    @PostMapping
    public ResponseEntity<KitchenTypeResponse> createKitchenType(
            @Validated(OnCreateGroup.class) @RequestBody KitchenTypeRequest request) {
        KitchenTypeResponse response = kitchenTypeUseCase.createKitchenType(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<KitchenTypeResponse>> getAllKitchenTypes() {
        List<KitchenTypeResponse> kitchenTypes = kitchenTypeUseCase.getAllKitchenTypes();
        return ResponseEntity.ok(kitchenTypes);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<KitchenTypeResponse> getKitchenTypeById(@PathVariable String id) {
        KitchenTypeResponse kitchenType = kitchenTypeUseCase.getKitchenTypeById(id);
        return ResponseEntity.ok(kitchenType);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<KitchenTypeResponse> updateKitchenType(
            @PathVariable String id,
            @Validated(OnCreateGroup.class) @RequestBody KitchenTypeRequest request) {
        KitchenTypeResponse response = kitchenTypeUseCase.updateKitchenType(id, request);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKitchenType(@PathVariable String id) {
        kitchenTypeUseCase.deleteKitchenType(id);
        return ResponseEntity.noContent().build();
    }
}
