package com.fiap.itmoura.tech_challenge_restaurant.domain.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KitchenTypeEntity {

    private String id;

    private String name;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdate;

    public static KitchenTypeEntity fromDocument(KitchenTypeDocumentEntity document) {
        return KitchenTypeEntity.builder()
            .id(document.getId())
            .name(document.getName())
            .description(document.getDescription())
            .createdAt(document.getCreatedAt())
            .lastUpdate(document.getLastUpdate())
            .build();
    }
}
