package com.fiap.itmoura.tech_challenge_restaurant.application.ports.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.RestaurantEntity;

@Repository
public interface RestaurantRepository extends MongoRepository<RestaurantEntity, UUID> {

    List<RestaurantEntity> findByIsActiveTrue();

    @Query("{ 'menu.items.id': ?0 }")
    Optional<RestaurantEntity> findByMenuItemId(UUID itemId);
    
    @Query("{ 'kitchenType.id': ?0 }")
    boolean existsByKitchenTypeId(UUID kitchenTypeId);
}
