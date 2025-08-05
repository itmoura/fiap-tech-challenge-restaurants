package com.fiap.itmoura.tech_challenge_restaurant.domain.entities;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemEntity {

    private String id;

    private String name;

    private String description;

    private BigDecimal price;

    private Boolean onlyForLocalConsumption;

    private String imagePath;

    private Boolean isActive;
}
