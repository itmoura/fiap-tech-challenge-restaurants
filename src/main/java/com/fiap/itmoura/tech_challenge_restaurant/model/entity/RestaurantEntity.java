package com.fiap.itmoura.tech_challenge_restaurant.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fiap.itmoura.tech_challenge_restaurant.model.data.OperationDaysTimeData;
import org.bson.types.ObjectId;

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
    private String id;
 
    private String name;

    private KitchenTypeEntity kitchenType;

    private String address;

    private List<OperationDaysTimeData> daysOperation;

    private String ownerId;

    private Boolean isActive;

    private Double rating;

    @LastModifiedDate
    LocalDateTime lastUpdate;
    
    @CreatedDate
    LocalDateTime createdAt;
}
