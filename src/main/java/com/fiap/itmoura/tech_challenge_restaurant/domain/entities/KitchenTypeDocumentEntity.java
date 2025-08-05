package com.fiap.itmoura.tech_challenge_restaurant.domain.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "kitchen_types")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KitchenTypeDocumentEntity {

    @Id
    @Field("_id")
    private String id;

    @Indexed(unique = true)
    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @CreatedDate
    @Field("createdAt")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("lastUpdate")
    private LocalDateTime lastUpdate;
}
