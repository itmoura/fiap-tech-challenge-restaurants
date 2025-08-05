package com.fiap.itmoura.tech_challenge_restaurant.presentation.controllers;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype.KitchenTypeRequest;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.KitchenTypeRepository;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.KitchenTypeDocumentEntity;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/debug")
@Slf4j
public class DebugController {

    @Autowired
    private KitchenTypeRepository kitchenTypeRepository;
    
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping("/kitchen-type-direct")
    public ResponseEntity<?> createKitchenTypeDirect(@RequestBody KitchenTypeRequest request) {
        log.info("=== DEBUG: Creating kitchen type directly ===");
        log.info("Request: name='{}', description='{}'", request.getName(), request.getDescription());
        
        // Criar entidade diretamente
        KitchenTypeDocumentEntity entity = new KitchenTypeDocumentEntity();
        entity.setId(UUID.randomUUID());
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setLastUpdate(LocalDateTime.now());
        
        log.info("Entity before save: {}", entity);
        
        // Salvar usando repository
        KitchenTypeDocumentEntity saved = kitchenTypeRepository.save(entity);
        log.info("Entity after save: {}", saved);
        
        // Buscar diretamente do MongoDB para verificar
        KitchenTypeDocumentEntity found = mongoTemplate.findById(saved.getId(), KitchenTypeDocumentEntity.class);
        log.info("Entity found in MongoDB: {}", found);
        
        return ResponseEntity.ok(saved);
    }
    
    @PostMapping("/kitchen-type-template")
    public ResponseEntity<?> createKitchenTypeWithTemplate(@RequestBody KitchenTypeRequest request) {
        log.info("=== DEBUG: Creating kitchen type with MongoTemplate ===");
        
        KitchenTypeDocumentEntity entity = KitchenTypeDocumentEntity.builder()
            .id(UUID.randomUUID())
            .name(request.getName())
            .description(request.getDescription())
            .createdAt(LocalDateTime.now())
            .lastUpdate(LocalDateTime.now())
            .build();
        
        log.info("Entity before save: {}", entity);
        
        // Salvar usando MongoTemplate diretamente
        KitchenTypeDocumentEntity saved = mongoTemplate.save(entity);
        log.info("Entity after save: {}", saved);
        
        return ResponseEntity.ok(saved);
    }
    
    @GetMapping("/kitchen-types-raw")
    public ResponseEntity<?> getAllKitchenTypesRaw() {
        log.info("=== DEBUG: Getting all kitchen types raw ===");
        
        // Buscar todos usando MongoTemplate
        var allEntities = mongoTemplate.findAll(KitchenTypeDocumentEntity.class);
        log.info("Found {} entities", allEntities.size());
        
        for (KitchenTypeDocumentEntity entity : allEntities) {
            log.info("Entity: {}", entity);
        }
        
        return ResponseEntity.ok(allEntities);
    }
    
    @GetMapping("/kitchen-types-by-name")
    public ResponseEntity<?> getKitchenTypeByName() {
        log.info("=== DEBUG: Getting kitchen type by name ===");
        
        Query query = new Query(Criteria.where("name").is("Teste Cozinha"));
        KitchenTypeDocumentEntity found = mongoTemplate.findOne(query, KitchenTypeDocumentEntity.class);
        
        log.info("Found entity by name: {}", found);
        
        return ResponseEntity.ok(found);
    }
}
