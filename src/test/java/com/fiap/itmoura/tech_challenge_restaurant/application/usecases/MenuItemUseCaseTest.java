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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
@DisplayName("MenuItemUseCase Tests")
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
            .isActive(true)
            .build();
    }

    @Nested
    @DisplayName("Create Menu Item Tests")
    class CreateMenuItemTests {

        @Test
        @DisplayName("Should create menu item successfully")
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
            assertEquals("restaurant1", result.restaurantId());
            assertTrue(result.isActive());
            
            verify(restaurantRepository).findById("restaurant1");
            verify(menuItemRepository).save(any(MenuItemEntity.class));
        }

        @Test
        @DisplayName("Should throw NotFoundException when restaurant not exists")
        void shouldThrowNotFoundWhenRestaurantNotExists() {
            // Given
            when(restaurantRepository.findById(anyString())).thenReturn(Optional.empty());

            // When & Then
            NotFoundException exception = assertThrows(NotFoundException.class, 
                () -> menuItemUseCase.createMenuItem(menuItemDTO));
            
            assertEquals("Restaurant not found", exception.getMessage());
            verify(restaurantRepository).findById("restaurant1");
            verify(menuItemRepository, never()).save(any(MenuItemEntity.class));
        }

        @Test
        @DisplayName("Should set default values when creating menu item")
        void shouldSetDefaultValuesWhenCreatingMenuItem() {
            // Given
            MenuItemDTO dtoWithNulls = new MenuItemDTO(
                null, "Pizza", "Pizza margherita", new BigDecimal("30.00"), 
                null, null, "restaurant1", null
            );
            
            when(restaurantRepository.findById(anyString())).thenReturn(Optional.of(restaurantEntity));
            when(menuItemRepository.save(any(MenuItemEntity.class))).thenAnswer(invocation -> {
                MenuItemEntity entity = invocation.getArgument(0);
                entity.setId("2");
                return entity;
            });

            // When
            menuItemUseCase.createMenuItem(dtoWithNulls);

            // Then
            verify(menuItemRepository).save(argThat(entity -> 
                !entity.getOnlyForLocalConsumption() && entity.getIsActive()
            ));
        }
    }

    @Nested
    @DisplayName("Get Menu Item Tests")
    class GetMenuItemTests {

        @Test
        @DisplayName("Should get menu item by ID successfully")
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
        @DisplayName("Should throw NotFoundException when menu item not exists")
        void shouldThrowNotFoundWhenMenuItemNotExists() {
            // Given
            when(menuItemRepository.findById(anyString())).thenReturn(Optional.empty());

            // When & Then
            NotFoundException exception = assertThrows(NotFoundException.class, 
                () -> menuItemUseCase.getMenuItemById("nonexistent"));
            
            assertEquals("Menu item not found", exception.getMessage());
            verify(menuItemRepository).findById("nonexistent");
        }

        @Test
        @DisplayName("Should get all menu items successfully")
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
        @DisplayName("Should get active menu items successfully")
        void shouldGetActiveMenuItemsSuccessfully() {
            // Given
            List<MenuItemEntity> menuItems = Arrays.asList(menuItemEntity);
            when(menuItemRepository.findByIsActiveTrue()).thenReturn(menuItems);

            // When
            List<MenuItemDTO> result = menuItemUseCase.getActiveMenuItems();

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertTrue(result.get(0).isActive());
            verify(menuItemRepository).findByIsActiveTrue();
        }

        @Test
        @DisplayName("Should get menu items by restaurant successfully")
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
            assertEquals("restaurant1", result.get(0).restaurantId());
            verify(restaurantRepository).findById("restaurant1");
            verify(menuItemRepository).findByRestaurantId("restaurant1");
        }

        @Test
        @DisplayName("Should get active menu items by restaurant successfully")
        void shouldGetActiveMenuItemsByRestaurantSuccessfully() {
            // Given
            List<MenuItemEntity> menuItems = Arrays.asList(menuItemEntity);
            when(restaurantRepository.findById(anyString())).thenReturn(Optional.of(restaurantEntity));
            when(menuItemRepository.findByRestaurantIdAndIsActiveTrue(anyString())).thenReturn(menuItems);

            // When
            List<MenuItemDTO> result = menuItemUseCase.getActiveMenuItemsByRestaurant("restaurant1");

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertTrue(result.get(0).isActive());
            verify(restaurantRepository).findById("restaurant1");
            verify(menuItemRepository).findByRestaurantIdAndIsActiveTrue("restaurant1");
        }
    }

    @Nested
    @DisplayName("Update Menu Item Tests")
    class UpdateMenuItemTests {

        @Test
        @DisplayName("Should update menu item successfully")
        void shouldUpdateMenuItemSuccessfully() {
            // Given
            MenuItemDTO updateDTO = new MenuItemDTO(
                "1", "Hambúrguer Premium", "Hambúrguer premium com ingredientes especiais", 
                new BigDecimal("35.90"), false, "/images/hamburguer-premium.jpg", 
                "restaurant1", true
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
        @DisplayName("Should validate restaurant when changing restaurant ID")
        void shouldValidateRestaurantWhenChangingRestaurantId() {
            // Given
            MenuItemDTO updateDTO = new MenuItemDTO(
                "1", "Pizza", "Pizza margherita", new BigDecimal("30.00"), 
                false, "/images/pizza.jpg", "restaurant2", true
            );
            
            RestaurantEntity newRestaurant = RestaurantEntity.builder()
                .id("restaurant2")
                .name("Novo Restaurante")
                .build();
            
            when(menuItemRepository.findById(anyString())).thenReturn(Optional.of(menuItemEntity));
            when(restaurantRepository.findById("restaurant2")).thenReturn(Optional.of(newRestaurant));
            when(menuItemRepository.save(any(MenuItemEntity.class))).thenReturn(menuItemEntity);

            // When
            menuItemUseCase.updateMenuItem("1", updateDTO);

            // Then
            verify(restaurantRepository).findById("restaurant2");
            verify(menuItemRepository).save(any(MenuItemEntity.class));
        }

        @Test
        @DisplayName("Should throw NotFoundException when updating non-existent menu item")
        void shouldThrowNotFoundWhenUpdatingNonExistentMenuItem() {
            // Given
            MenuItemDTO updateDTO = new MenuItemDTO(
                "1", "Pizza", "Pizza margherita", new BigDecimal("30.00"), 
                false, "/images/pizza.jpg", "restaurant1", true
            );
            
            when(menuItemRepository.findById(anyString())).thenReturn(Optional.empty());

            // When & Then
            NotFoundException exception = assertThrows(NotFoundException.class, 
                () -> menuItemUseCase.updateMenuItem("1", updateDTO));
            
            assertEquals("Menu item not found", exception.getMessage());
            verify(menuItemRepository).findById("1");
            verify(menuItemRepository, never()).save(any(MenuItemEntity.class));
        }
    }

    @Nested
    @DisplayName("Toggle Status Tests")
    class ToggleStatusTests {

        @Test
        @DisplayName("Should toggle menu item status from active to inactive")
        void shouldToggleMenuItemStatusFromActiveToInactive() {
            // Given
            MenuItemEntity activeItem = MenuItemEntity.builder()
                .id("1")
                .name("Pizza")
                .isActive(true)
                .build();
            
            MenuItemEntity inactiveItem = MenuItemEntity.builder()
                .id("1")
                .name("Pizza")
                .isActive(false)
                .build();
            
            when(menuItemRepository.findById(anyString())).thenReturn(Optional.of(activeItem));
            when(menuItemRepository.save(any(MenuItemEntity.class))).thenReturn(inactiveItem);

            // When
            MenuItemDTO result = menuItemUseCase.toggleMenuItemStatus("1");

            // Then
            assertNotNull(result);
            verify(menuItemRepository).findById("1");
            verify(menuItemRepository).save(argThat(entity -> !entity.getIsActive()));
        }

        @Test
        @DisplayName("Should toggle menu item status from inactive to active")
        void shouldToggleMenuItemStatusFromInactiveToActive() {
            // Given
            MenuItemEntity inactiveItem = MenuItemEntity.builder()
                .id("1")
                .name("Pizza")
                .isActive(false)
                .build();
            
            MenuItemEntity activeItem = MenuItemEntity.builder()
                .id("1")
                .name("Pizza")
                .isActive(true)
                .build();
            
            when(menuItemRepository.findById(anyString())).thenReturn(Optional.of(inactiveItem));
            when(menuItemRepository.save(any(MenuItemEntity.class))).thenReturn(activeItem);

            // When
            MenuItemDTO result = menuItemUseCase.toggleMenuItemStatus("1");

            // Then
            assertNotNull(result);
            verify(menuItemRepository).save(argThat(entity -> entity.getIsActive()));
        }
    }

    @Nested
    @DisplayName("Delete Menu Item Tests")
    class DeleteMenuItemTests {

        @Test
        @DisplayName("Should delete menu item successfully")
        void shouldDeleteMenuItemSuccessfully() {
            // Given
            when(menuItemRepository.findById(anyString())).thenReturn(Optional.of(menuItemEntity));

            // When
            menuItemUseCase.deleteMenuItem("1");

            // Then
            verify(menuItemRepository).findById("1");
            verify(menuItemRepository).delete(menuItemEntity);
        }

        @Test
        @DisplayName("Should throw NotFoundException when deleting non-existent menu item")
        void shouldThrowNotFoundWhenDeletingNonExistentMenuItem() {
            // Given
            when(menuItemRepository.findById(anyString())).thenReturn(Optional.empty());

            // When & Then
            NotFoundException exception = assertThrows(NotFoundException.class, 
                () -> menuItemUseCase.deleteMenuItem("1"));
            
            assertEquals("Menu item not found", exception.getMessage());
            verify(menuItemRepository).findById("1");
            verify(menuItemRepository, never()).delete(any(MenuItemEntity.class));
        }
    }
}
