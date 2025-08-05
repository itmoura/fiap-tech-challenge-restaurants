package com.fiap.itmoura.tech_challenge_restaurant.application.usecases;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KitchenTypeUseCase {

    private final KitchenTypeRepository kitchenTypeRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public KitchenTypeResponse createKitchenType(KitchenTypeRequest request) {
        log.info("=== Creating kitchen type ===");
        log.info("Request received: name='{}', description='{}'", request.getName(), request.getDescription());

        // Validar entrada
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Kitchen type name cannot be null or empty");
        }

        String trimmedName = request.getName().trim();
        String trimmedDescription = request.getDescription() != null ? request.getDescription().trim() : null;

        log.info("After trimming: name='{}', description='{}'", trimmedName, trimmedDescription);

        // Verificar se já existe um tipo de cozinha com o mesmo nome
        if (kitchenTypeRepository.existsByNameIgnoreCase(trimmedName)) {
            log.warn("Kitchen type with name '{}' already exists", trimmedName);
            throw new ConflictRequestException("Kitchen type with name '" + trimmedName + "' already exists");
        }

        // Criar entidade
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        
        KitchenTypeDocumentEntity entity = KitchenTypeDocumentEntity.builder()
            .id(id)
            .name(trimmedName)
            .description(trimmedDescription)
            .createdAt(now)
            .lastUpdate(now)
            .build();

        log.info("Entity created: {}", entity);
        log.info("Entity details - ID: {}, Name: '{}', Description: '{}', CreatedAt: {}, LastUpdate: {}", 
                entity.getId(), entity.getName(), entity.getDescription(), entity.getCreatedAt(), entity.getLastUpdate());

        // Salvar no banco
        KitchenTypeDocumentEntity savedEntity = kitchenTypeRepository.save(entity);
        log.info("Entity saved: {}", savedEntity);
        log.info("Saved entity details - ID: {}, Name: '{}', Description: '{}', CreatedAt: {}, LastUpdate: {}", 
                savedEntity.getId(), savedEntity.getName(), savedEntity.getDescription(), 
                savedEntity.getCreatedAt(), savedEntity.getLastUpdate());

        // Verificar se foi salvo corretamente
        var foundEntity = kitchenTypeRepository.findById(savedEntity.getId());
        if (foundEntity.isPresent()) {
            log.info("Verification: Entity found in database: {}", foundEntity.get());
        } else {
            log.error("Verification: Entity NOT found in database after save!");
        }

        // Converter para response
        KitchenTypeResponse response = KitchenTypeResponse.fromEntity(savedEntity);
        log.info("Response created: {}", response);

        return response;
    }

    public List<KitchenTypeResponse> getAllKitchenTypes() {
        log.info("=== Getting all kitchen types ===");
        
        List<KitchenTypeDocumentEntity> entities = kitchenTypeRepository.findAll();
        log.info("Found {} kitchen types in database", entities.size());
        
        for (KitchenTypeDocumentEntity entity : entities) {
            log.info("Entity from DB: {}", entity);
        }
        
        List<KitchenTypeResponse> responses = entities.stream()
            .map(KitchenTypeResponse::fromEntity)
            .collect(Collectors.toList());
            
        log.info("Returning {} responses", responses.size());
        return responses;
    }

    public KitchenTypeResponse getKitchenTypeById(UUID id) {
        log.info("=== Getting kitchen type by ID: {} ===", id);
        
        KitchenTypeDocumentEntity entity = kitchenTypeRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Kitchen type not found with ID: {}", id);
                return new NotFoundException("Kitchen type not found with id: " + id);
            });
            
        log.info("Found entity: {}", entity);
        
        KitchenTypeResponse response = KitchenTypeResponse.fromEntity(entity);
        log.info("Returning response: {}", response);
        
        return response;
    }

    @Transactional
    public KitchenTypeResponse updateKitchenType(UUID id, KitchenTypeRequest request) {
        log.info("=== Updating kitchen type with ID: {} ===", id);
        log.info("Update request: name='{}', description='{}'", request.getName(), request.getDescription());

        KitchenTypeDocumentEntity existingEntity = kitchenTypeRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Kitchen type not found with id: " + id));

        log.info("Existing entity: {}", existingEntity);

        String trimmedName = request.getName().trim();
        String trimmedDescription = request.getDescription() != null ? request.getDescription().trim() : null;

        // Verificar se o nome já existe (exceto para o próprio registro)
        if (kitchenTypeRepository.existsByNameIgnoreCaseAndIdNot(trimmedName, id)) {
            throw new ConflictRequestException("Kitchen type with name '" + trimmedName + "' already exists");
        }

        // Atualizar campos
        existingEntity.setName(trimmedName);
        existingEntity.setDescription(trimmedDescription);
        existingEntity.setLastUpdate(LocalDateTime.now());

        log.info("Entity before update: {}", existingEntity);

        KitchenTypeDocumentEntity updatedEntity = kitchenTypeRepository.save(existingEntity);
        log.info("Entity after update: {}", updatedEntity);

        return KitchenTypeResponse.fromEntity(updatedEntity);
    }

    @Transactional
    public void deleteKitchenType(UUID id) {
        log.info("=== Deleting kitchen type with ID: {} ===", id);

        KitchenTypeDocumentEntity entity = kitchenTypeRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Kitchen type not found with id: " + id));

        log.info("Entity to delete: {}", entity);

        // Verificar se está sendo usado por algum restaurante
        boolean isUsedByRestaurants = restaurantRepository.existsByKitchenTypeId(id);
        if (isUsedByRestaurants) {
            log.warn("Cannot delete kitchen type {} - it is being used by restaurants", id);
            throw new ConflictRequestException("Cannot delete kitchen type. It is being used by restaurants");
        }

        kitchenTypeRepository.deleteById(id);
        log.info("Kitchen type deleted successfully");
    }
}
