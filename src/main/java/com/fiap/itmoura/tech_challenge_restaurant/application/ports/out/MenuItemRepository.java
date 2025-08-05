package com.fiap.itmoura.tech_challenge_restaurant.application.ports.out;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.MenuItemEntity;

@Repository
public interface MenuItemRepository extends MongoRepository<MenuItemEntity, String> {

    List<MenuItemEntity> findByRestaurantId(String restaurantId);
    
    List<MenuItemEntity> findByRestaurantIdAndIsActiveTrue(String restaurantId);
    
    List<MenuItemEntity> findByIsActiveTrue();
}
