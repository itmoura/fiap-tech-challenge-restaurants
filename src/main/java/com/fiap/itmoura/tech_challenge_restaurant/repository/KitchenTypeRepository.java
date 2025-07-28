package com.fiap.itmoura.tech_challenge_restaurant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.fiap.itmoura.tech_challenge_restaurant.model.entity.KitchenTypeEntity;

@Repository
public interface KitchenTypeRepository extends MongoRepository<KitchenTypeEntity, String> {

    Boolean existsByName(String name);

    @Query("{ $or: [ { 'id': ?0 }, { 'name': ?0 } ] }")
    Optional<KitchenTypeEntity> findByIdOrName(String idOrName);

}