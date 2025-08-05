package com.fiap.itmoura.tech_challenge_restaurant.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.menuitem.MenuItemDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.MenuItemRepository;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.RestaurantRepository;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.MenuItemEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.RestaurantEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
class MenuItemUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private MenuItemUseCase menuItemUseCase;

    private MenuItemDTO menuItemDTO;
    private MenuItemEntity menuItemEntity;
    private RestaurantEntity restaurantEntity;

    @BeforeEach
    void setUp() {
        menuItemDTO = new MenuItemDTO(
            "1", 
            "Hambúrguer Artesanal", 
            "Hambúrguer com carne artesanal", 
            new BigDecimal("25.90"), 
            false, 
            "/images/hamburguer.jpg", 
            "restaurant1", 
            true
        );
        
        menuItemEntity = MenuItemEntity.builder()
            .id("1")
            .name("Hambúrguer Artesanal")
            .description("Hambúrguer com carne artesanal")
            .price(new BigDecimal("25.90"))
            .onlyForLocalConsumption(false)
            .imagePath("/images/hamburguer.jpg")
            .restaurantId("restaurant1")
            .isActive(true)
            .createdAt(LocalDateTime.now())
            .lastUpdate(LocalDateTime.now())
            .build();
            
        restaurantEntity = RestaurantEntity.builder()
            .id("restaurant1")
            .name("Restaurante Teste")
            .build();
    }

    @Test
    void shouldCreateMenuItemSuccessfully() {
        // Given
        when(restaurantRepository.findById(anyString())).thenReturn(Optional.of(restaurantEntity));
        when(menuItemRepository.save(any(MenuItemEntity.class))).thenReturn(menuItemEntity);

        // When
        MenuItemDTO result = menuItemUseCase.createMenuItem(menuItemDTO);

        // Then
        assertNotNull(result);
        assertEquals("1", result.id());
        assertEquals("Hambúrguer Artesanal", result.name());
        assertEquals(new BigDecimal("25.90"), result.price());
        verify(restaurantRepository).findById("restaurant1");
        verify(menuItemRepository).save(any(MenuItemEntity.class));
    }

    @Test
    void shouldThrowNotFoundWhenRestaurantNotExists() {
        // Given
        when(restaurantRepository.findById(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, 
            () -> menuItemUseCase.createMenuItem(menuItemDTO));
        
        verify(restaurantRepository).findById("restaurant1");
        verify(menuItemRepository, never()).save(any(MenuItemEntity.class));
    }

    @Test
    void shouldGetMenuItemByIdSuccessfully() {
        // Given
        when(menuItemRepository.findById(anyString())).thenReturn(Optional.of(menuItemEntity));

        // When
        MenuItemEntity result = menuItemUseCase.getMenuItemById("1");

        // Then
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Hambúrguer Artesanal", result.getName());
        verify(menuItemRepository).findById("1");
    }

    @Test
    void shouldGetAllMenuItemsSuccessfully() {
        // Given
        List<MenuItemEntity> menuItems = Arrays.asList(menuItemEntity);
        when(menuItemRepository.findAll()).thenReturn(menuItems);

        // When
        List<MenuItemDTO> result = menuItemUseCase.getAllMenuItems();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).id());
        verify(menuItemRepository).findAll();
    }

    @Test
    void shouldGetActiveMenuItemsSuccessfully() {
        // Given
        List<MenuItemEntity> menuItems = Arrays.asList(menuItemEntity);
        when(menuItemRepository.findByIsActiveTrue()).thenReturn(menuItems);

        // When
        List<MenuItemDTO> result = menuItemUseCase.getActiveMenuItems();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).id());
        verify(menuItemRepository).findByIsActiveTrue();
    }

    @Test
    void shouldGetMenuItemsByRestaurantSuccessfully() {
        // Given
        List<MenuItemEntity> menuItems = Arrays.asList(menuItemEntity);
        when(restaurantRepository.findById(anyString())).thenReturn(Optional.of(restaurantEntity));
        when(menuItemRepository.findByRestaurantId(anyString())).thenReturn(menuItems);

        // When
        List<MenuItemDTO> result = menuItemUseCase.getMenuItemsByRestaurant("restaurant1");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).id());
        verify(restaurantRepository).findById("restaurant1");
        verify(menuItemRepository).findByRestaurantId("restaurant1");
    }

    @Test
    void shouldUpdateMenuItemSuccessfully() {
        // Given
        MenuItemDTO updateDTO = new MenuItemDTO(
            "1", 
            "Hambúrguer Premium", 
            "Hambúrguer premium com ingredientes especiais", 
            new BigDecimal("35.90"), 
            false, 
            "/images/hamburguer-premium.jpg", 
            "restaurant1", 
            true
        );
        when(menuItemRepository.findById(anyString())).thenReturn(Optional.of(menuItemEntity));
        when(menuItemRepository.save(any(MenuItemEntity.class))).thenReturn(menuItemEntity);

        // When
        MenuItemDTO result = menuItemUseCase.updateMenuItem("1", updateDTO);

        // Then
        assertNotNull(result);
        verify(menuItemRepository).findById("1");
        verify(menuItemRepository).save(any(MenuItemEntity.class));
    }

    @Test
    void shouldToggleMenuItemStatusSuccessfully() {
        // Given
        when(menuItemRepository.findById(anyString())).thenReturn(Optional.of(menuItemEntity));
        when(menuItemRepository.save(any(MenuItemEntity.class))).thenReturn(menuItemEntity);

        // When
        MenuItemDTO result = menuItemUseCase.toggleMenuItemStatus("1");

        // Then
        assertNotNull(result);
        verify(menuItemRepository).findById("1");
        verify(menuItemRepository).save(any(MenuItemEntity.class));
    }

    @Test
    void shouldDeleteMenuItemSuccessfully() {
        // Given
        when(menuItemRepository.findById(anyString())).thenReturn(Optional.of(menuItemEntity));

        // When
        menuItemUseCase.deleteMenuItem("1");

        // Then
        verify(menuItemRepository).findById("1");
        verify(menuItemRepository).delete(menuItemEntity);
    }
}
