package com.fiap.itmoura.tech_challenge_restaurant.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fiap.itmoura.tech_challenge_restaurant.model.entity.RestaurantEntity;

@Repository
public interface RestaurantRepository extends MongoRepository<RestaurantEntity, String> {

}