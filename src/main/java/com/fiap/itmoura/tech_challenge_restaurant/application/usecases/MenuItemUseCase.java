package com.fiap.itmoura.tech_challenge_restaurant.application.usecases;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.menuitem.MenuItemDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.MenuItemRepository;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.RestaurantRepository;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.MenuItemEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor    
public class MenuItemUseCase {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public MenuItemDTO createMenuItem(MenuItemDTO menuItemDTO) {
        log.info("Creating menu item: {}", menuItemDTO);

        // Verificar se o restaurante existe
        restaurantRepository.findById(menuItemDTO.restaurantId())
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        MenuItemEntity menuItemEntity = menuItemDTO.toEntity();
        menuItemEntity.setLastUpdate(LocalDateTime.now());
        menuItemEntity.setCreatedAt(LocalDateTime.now());

        var menuItemSaved = menuItemRepository.save(menuItemEntity);

        return MenuItemDTO.fromEntity(menuItemSaved);
    }

    public MenuItemEntity getMenuItemById(String id) {
        return menuItemRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Menu item not found"));
    }

    public List<MenuItemDTO> getAllMenuItems() {
        log.info("Getting all menu items");
        return menuItemRepository.findAll()
            .stream()
            .map(MenuItemDTO::fromEntity)
            .toList();
    }

    public List<MenuItemDTO> getActiveMenuItems() {
        log.info("Getting active menu items");
        return menuItemRepository.findByIsActiveTrue()
            .stream()
            .map(MenuItemDTO::fromEntity)
            .toList();
    }

    public List<MenuItemDTO> getMenuItemsByRestaurant(String restaurantId) {
        log.info("Getting menu items by restaurant: {}", restaurantId);
        
        // Verificar se o restaurante existe
        restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));
        
        return menuItemRepository.findByRestaurantId(restaurantId)
            .stream()
            .map(MenuItemDTO::fromEntity)
            .toList();
    }

    public List<MenuItemDTO> getActiveMenuItemsByRestaurant(String restaurantId) {
        log.info("Getting active menu items by restaurant: {}", restaurantId);
        
        // Verificar se o restaurante existe
        restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));
        
        return menuItemRepository.findByRestaurantIdAndIsActiveTrue(restaurantId)
            .stream()
            .map(MenuItemDTO::fromEntity)
            .toList();
    }

    @Transactional
    public MenuItemDTO updateMenuItem(String id, MenuItemDTO menuItemDTO) {
        MenuItemEntity menuItemEntity = getMenuItemById(id);
        
        // Verificar se o restaurante existe (se foi alterado)
        if (!menuItemEntity.getRestaurantId().equals(menuItemDTO.restaurantId())) {
            restaurantRepository.findById(menuItemDTO.restaurantId())
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));
        }
        
        menuItemEntity.setName(menuItemDTO.name());
        menuItemEntity.setDescription(menuItemDTO.description());
        menuItemEntity.setPrice(menuItemDTO.price());
        menuItemEntity.setOnlyForLocalConsumption(menuItemDTO.onlyForLocalConsumption() != null ? menuItemDTO.onlyForLocalConsumption() : false);
        menuItemEntity.setImagePath(menuItemDTO.imagePath());
        menuItemEntity.setRestaurantId(menuItemDTO.restaurantId());
        menuItemEntity.setIsActive(menuItemDTO.isActive() != null ? menuItemDTO.isActive() : true);
        menuItemEntity.setLastUpdate(LocalDateTime.now());

        var menuItemSaved = menuItemRepository.save(menuItemEntity);

        return MenuItemDTO.fromEntity(menuItemSaved);
    }

    @Transactional
    public void deleteMenuItem(String id) {
        MenuItemEntity menuItemEntity = getMenuItemById(id);
        menuItemRepository.delete(menuItemEntity);
    }

    @Transactional
    public MenuItemDTO toggleMenuItemStatus(String id) {
        MenuItemEntity menuItemEntity = getMenuItemById(id);
        menuItemEntity.setIsActive(!menuItemEntity.getIsActive());
        menuItemEntity.setLastUpdate(LocalDateTime.now());

        var menuItemSaved = menuItemRepository.save(menuItemEntity);

        return MenuItemDTO.fromEntity(menuItemSaved);
    }
}
