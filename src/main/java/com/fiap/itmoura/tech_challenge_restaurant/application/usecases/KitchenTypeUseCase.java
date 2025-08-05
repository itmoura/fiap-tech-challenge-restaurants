package com.fiap.itmoura.tech_challenge_restaurant.application.usecases;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype.KitchenTypeRequest;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype.KitchenTypeResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.KitchenTypeRepository;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.RestaurantRepository;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.KitchenTypeDocumentEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.ConflictRequestException;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class KitchenTypeUseCase {

    private final KitchenTypeRepository kitchenTypeRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public KitchenTypeResponse createKitchenType(KitchenTypeRequest request) {
        log.info("Creating kitchen type: {}", request.getName());

        // Verificar se já existe um tipo de cozinha com o mesmo nome
        if (kitchenTypeRepository.existsByNameIgnoreCase(request.getName())) {
            throw new ConflictRequestException("Kitchen type with name '" + request.getName() + "' already exists");
        }

        KitchenTypeDocumentEntity entity = KitchenTypeDocumentEntity.builder()
            .id(UUID.randomUUID())
            .name(request.getName().trim())
            .description(request.getDescription() != null ? request.getDescription().trim() : null)
            .createdAt(LocalDateTime.now())
            .lastUpdate(LocalDateTime.now())
            .build();

        KitchenTypeDocumentEntity savedEntity = kitchenTypeRepository.save(entity);
        log.info("Kitchen type created successfully with ID: {}", savedEntity.getId());

        return KitchenTypeResponse.fromEntity(savedEntity);
    }

    public List<KitchenTypeResponse> getAllKitchenTypes() {
        log.info("Fetching all kitchen types");

        List<KitchenTypeDocumentEntity> entities = kitchenTypeRepository.findAll();

        return entities.stream()
            .map(KitchenTypeResponse::fromEntity)
            .toList();
    }

    public KitchenTypeResponse getKitchenTypeById(UUID id) {
        log.info("Fetching kitchen type by ID: {}", id);

        KitchenTypeDocumentEntity entity = kitchenTypeRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Kitchen type not found with ID: " + id));

        return KitchenTypeResponse.fromEntity(entity);
    }

    @Transactional
    public KitchenTypeResponse updateKitchenType(UUID id, KitchenTypeRequest request) {
        log.info("Updating kitchen type with ID: {}", id);

        KitchenTypeDocumentEntity existingEntity = kitchenTypeRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Kitchen type not found with ID: " + id));

        // Verificar se o novo nome já existe (excluindo o próprio registro)
        if (kitchenTypeRepository.existsByNameIgnoreCaseAndIdNot(request.getName(), id)) {
            throw new ConflictRequestException("Kitchen type with name '" + request.getName() + "' already exists");
        }

        existingEntity.setName(request.getName().trim());
        existingEntity.setDescription(request.getDescription() != null ? request.getDescription().trim() : null);
        existingEntity.setLastUpdate(LocalDateTime.now());

        KitchenTypeDocumentEntity updatedEntity = kitchenTypeRepository.save(existingEntity);
        log.info("Kitchen type updated successfully with ID: {}", updatedEntity.getId());

        return KitchenTypeResponse.fromEntity(updatedEntity);
    }

    @Transactional
    public void deleteKitchenType(UUID id) {
        log.info("Deleting kitchen type with ID: {}", id);

        if (!kitchenTypeRepository.existsById(id)) {
            throw new NotFoundException("Kitchen type not found with ID: " + id);
        }

        // Verificar se o tipo de cozinha está sendo usado por algum restaurante
        boolean isUsedByRestaurants = restaurantRepository.findAll().stream()
            .anyMatch(restaurant -> restaurant.getKitchenType() != null && 
                     restaurant.getKitchenType().getId().equals(id));

        if (isUsedByRestaurants) {
            throw new ConflictRequestException("Cannot delete kitchen type as it is being used by restaurants");
        }

        kitchenTypeRepository.deleteById(id);
        log.info("Kitchen type deleted successfully with ID: {}", id);
    }

    public KitchenTypeDocumentEntity getKitchenTypeByIdOrName(String idOrName) {
        log.info("Fetching kitchen type by ID or name: {}", idOrName);

        // Tentar buscar por UUID primeiro
        try {
            UUID id = UUID.fromString(idOrName);
            return kitchenTypeRepository.findById(id).orElse(null);
        } catch (IllegalArgumentException e) {
            // Se não for UUID válido, buscar por nome
            return kitchenTypeRepository.findByNameIgnoreCase(idOrName).orElse(null);
        }
    }
}
