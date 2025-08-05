package com.fiap.itmoura.tech_challenge_restaurant.domain.entities;

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

    private UUID id;

    private String name;

    private String description;
}
