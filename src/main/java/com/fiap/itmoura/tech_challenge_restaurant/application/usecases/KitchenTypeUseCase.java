package com.fiap.itmoura.tech_challenge_restaurant.application.usecases;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype.KitchenTypeDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.KitchenTypeRepository;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.KitchenTypeEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.ConflictRequestException;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor    
public class KitchenTypeUseCase {

    private final KitchenTypeRepository kitchenTypeRepository;

    @Transactional
    public KitchenTypeDTO createKitchenType(KitchenTypeDTO kitchenTypeDTO) {
        log.info("Creating kitchen type: {}", kitchenTypeDTO);

        if (kitchenTypeRepository.existsByName(kitchenTypeDTO.name())) {
            throw new ConflictRequestException("Kitchen type already exists");
        }

        KitchenTypeEntity kitchenTypeEntity = kitchenTypeDTO.toEntity();
        kitchenTypeEntity.setName(kitchenTypeDTO.name().toUpperCase());
        kitchenTypeEntity.setLastUpdate(LocalDateTime.now());
        kitchenTypeEntity.setCreatedAt(LocalDateTime.now());

        var kitchenTypeSaved = kitchenTypeRepository.save(kitchenTypeEntity);

        return KitchenTypeDTO.fromEntity(kitchenTypeSaved);
    }

    public KitchenTypeEntity getKitchenTypeByIdOrName(String idOrName) {
        return kitchenTypeRepository.findByIdOrName(idOrName)
        .orElseThrow(() -> new NotFoundException("Kitchen type not found"));
    }

    @Transactional
    public KitchenTypeDTO updateKitchenType(String id, KitchenTypeDTO kitchenTypeDTO) {
        KitchenTypeEntity kitchenTypeEntity = getKitchenTypeByIdOrName(id);
        kitchenTypeEntity.setName(kitchenTypeDTO.name().toUpperCase());
        kitchenTypeEntity.setLastUpdate(LocalDateTime.now());

        var kitchenTypeSaved = kitchenTypeRepository.save(kitchenTypeEntity);

        return KitchenTypeDTO.fromEntity(kitchenTypeSaved);
    }

    @Transactional
    public void deleteKitchenType(String id) {
        KitchenTypeEntity kitchenTypeEntity = getKitchenTypeByIdOrName(id);
        kitchenTypeRepository.delete(kitchenTypeEntity);
    }
}