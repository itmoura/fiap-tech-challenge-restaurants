package com.fiap.itmoura.tech_challenge_restaurant.domain.entities;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuCategoryEntity {
    
    private UUID id;
    
    private String type;
    
    private List<MenuItemEntity> items;
}
