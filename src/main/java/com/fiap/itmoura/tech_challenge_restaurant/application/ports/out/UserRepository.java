package com.fiap.itmoura.tech_challenge_restaurant.application.ports.out;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.UserEntity;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {

    Boolean existsByEmail(String email);
    
    List<UserEntity> findByUserTypeId(String userTypeId);
}
