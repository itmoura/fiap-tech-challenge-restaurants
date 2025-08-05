package com.fiap.itmoura.tech_challenge_restaurant.application.ports.out;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.KitchenTypeDocumentEntity;

@Repository
public interface KitchenTypeRepository extends MongoRepository<KitchenTypeDocumentEntity, String> {

    Optional<KitchenTypeDocumentEntity> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, String id);
}
