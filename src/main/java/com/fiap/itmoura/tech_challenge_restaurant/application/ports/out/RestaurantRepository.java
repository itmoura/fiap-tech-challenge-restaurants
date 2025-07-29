package com.fiap.itmoura.tech_challenge_restaurant.application.ports.out;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.RestaurantEntity;

@Repository
public interface RestaurantRepository extends MongoRepository<RestaurantEntity, String> {

    List<RestaurantEntity> findByIsActiveTrue();
}