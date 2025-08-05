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

        // Verificar se já existe um tipo de cozinha com o mesmo nome
        if (kitchenTypeRepository.existsByNameIgnoreCase(trimmedName)) {
            log.warn("Kitchen type with name '{}' already exists", trimmedName);
            throw new ConflictRequestException("Kitchen type with name '" + trimmedName + "' already exists");
        }

        // Criar entidade
        String id = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();
        
        KitchenTypeDocumentEntity entity = KitchenTypeDocumentEntity.builder()
            .id(id)
            .name(trimmedName)
            .description(request.getDescription() != null ? request.getDescription().trim() : null)
            .createdAt(now)
            .lastUpdate(now)
            .build();

        // Salvar no banco
        KitchenTypeDocumentEntity savedEntity = kitchenTypeRepository.save(entity);

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

    public KitchenTypeResponse getKitchenTypeById(String id) {
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
    public KitchenTypeResponse updateKitchenType(String id, KitchenTypeRequest request) {
        log.info("=== Updating kitchen type with ID: {} ===", id);
        log.info("Update request: name='{}', description='{}'", request.getName(), request.getDescription());

        KitchenTypeDocumentEntity existingEntity = kitchenTypeRepository.findById(id.toString())
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
    public void deleteKitchenType(String id) {
        log.info("=== Deleting kitchen type with ID: {} ===", id);

        KitchenTypeDocumentEntity entity = kitchenTypeRepository.findById(id.toString())
            .orElseThrow(() -> new NotFoundException("Kitchen type not found with id: " + id));

        log.info("Entity to delete: {}", entity);

        // Verificar se está sendo usado por algum restaurante
        boolean isUsedByRestaurants = restaurantRepository.existsByKitchenTypeId(id);
        if (isUsedByRestaurants) {
            log.warn("Cannot delete kitchen type {} - it is being used by restaurants", id);
            throw new ConflictRequestException("Cannot delete kitchen type. It is being used by restaurants");
        }

        kitchenTypeRepository.deleteById(id.toString());
        log.info("Kitchen type deleted successfully");
    }

    public KitchenTypeResponse getKitchenTypeByIdOrName(String name) {
        log.info("=== Getting kitchen type by ID or name: {} ===", name);

        KitchenTypeDocumentEntity entity = kitchenTypeRepository.findByNameIgnoreCase(name)
            .orElseThrow(() -> new NotFoundException("Kitchen type not found with name: " + name));

        log.info("Found entity: {}", entity);

        KitchenTypeResponse response = KitchenTypeResponse.fromEntity(entity);
        log.info("Returning response: {}", response);

        return response;
    }
}
