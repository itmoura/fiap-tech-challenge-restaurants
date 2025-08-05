package com.fiap.itmoura.tech_challenge_restaurant.application.ports.out;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.UserTypeEntity;

@Repository
public interface UserTypeRepository extends MongoRepository<UserTypeEntity, String> {

    Boolean existsByName(String name);

    @Query("{ $or: [ { 'id': ?0 }, { 'name': ?0 } ] }")
    Optional<UserTypeEntity> findByIdOrName(String idOrName);
}
