package com.fiap.itmoura.tech_challenge_restaurant.domain.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private UUID id;

    @Indexed(unique = true)
    private String name;

    private String description;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastUpdate;
}
