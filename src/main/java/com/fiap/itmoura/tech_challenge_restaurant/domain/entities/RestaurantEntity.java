package com.fiap.itmoura.tech_challenge_restaurant.domain.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "restaurants")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantEntity {

    @Id
    private UUID id;
 
    private String name;

    private String address;

    private KitchenTypeEntity kitchenType;

    private List<OperationDaysTimeData> daysOperation;

    private UUID ownerId;

    private Boolean isActive;

    private List<MenuCategoryEntity> menu;

    @LastModifiedDate
    private LocalDateTime lastUpdate;
    
    @CreatedDate
    private LocalDateTime createdAt;
}
