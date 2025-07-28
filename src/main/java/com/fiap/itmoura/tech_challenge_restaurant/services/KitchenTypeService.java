package com.fiap.itmoura.tech_challenge_restaurant.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fiap.itmoura.tech_challenge_restaurant.exception.ConflictRequestException;
import com.fiap.itmoura.tech_challenge_restaurant.exception.NotFoundException;
import com.fiap.itmoura.tech_challenge_restaurant.model.dto.KitchenTypeDTO;
import com.fiap.itmoura.tech_challenge_restaurant.model.entity.KitchenTypeEntity;
import com.fiap.itmoura.tech_challenge_restaurant.repository.KitchenTypeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor    
public class KitchenTypeService {

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
}