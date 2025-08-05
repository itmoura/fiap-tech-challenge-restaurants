package com.fiap.itmoura.tech_challenge_restaurant.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuItemRequest;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuItemResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuItemWithContextDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.RestaurantRepository;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.DayEnum;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.KitchenTypeEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.MenuCategoryEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.MenuItemEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.OperationDaysTimeData;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.RestaurantEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
class MenuItemUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private MenuItemUseCase menuItemUseCase;

    private RestaurantEntity restaurantEntity;
    private MenuItemRequest menuItemRequest;
    private String restaurantId;
    private String menuId;
    private String itemId;
    private String kitchenTypeId;
    private String ownerId;

    @BeforeEach
    void setUp() {
        restaurantId = UUID.randomUUID().toString();
        menuId = UUID.randomUUID().toString();
        itemId = UUID.randomUUID().toString();
        kitchenTypeId = UUID.randomUUID().toString();
        ownerId = UUID.randomUUID().toString();

        menuItemRequest = MenuItemRequest.builder()
            .name("Hambúrguer Artesanal")
            .description("Hambúrguer com carne artesanal")
            .price(new BigDecimal("25.90"))
            .onlyForLocalConsumption(false)
            .imagePath("/images/hamburguer.jpg")
            .isActive(true)
            .build();

        KitchenTypeEntity kitchenTypeEntity = KitchenTypeEntity.builder()
            .id(kitchenTypeId)
            .name("Americana")
            .description("Cozinha Americana")
            .build();

        MenuItemEntity existingItem = MenuItemEntity.builder()
            .id(itemId)
            .name("X-Bacon")
            .description("Hambúrguer com bacon")
            .price(new BigDecimal("22.50"))
            .onlyForLocalConsumption(false)
            .imagePath("/images/x-bacon.jpg")
            .isActive(true)
            .build();

        MenuCategoryEntity menuCategory = MenuCategoryEntity.builder()
            .id(menuId)
            .type("Lanche")
            .items(List.of(existingItem))
            .build();

        restaurantEntity = RestaurantEntity.builder()
            .id(restaurantId)
            .name("Burger House")
            .address("Rua dos Pinheiros, 789")
            .kitchenType(kitchenTypeEntity)
            .daysOperation(List.of(new OperationDaysTimeData(DayEnum.MONDAY, "11:00", "23:00")))
            .ownerId(ownerId)
            .isActive(true)
            .menu(List.of(menuCategory))
            .lastUpdate(LocalDateTime.now())
            .createdAt(LocalDateTime.now())
            .build();
    }

    @Test
    void shouldCreateMenuItemSuccessfully() {
        // Given
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);

        // When
        MenuItemResponse response = menuItemUseCase.createMenuItem(restaurantId, menuId, menuItemRequest);

        // Then
        assertNotNull(response);
        assertEquals("Hambúrguer Artesanal", response.getName());
        assertEquals("Hambúrguer com carne artesanal", response.getDescription());
        assertEquals(new BigDecimal("25.90"), response.getPrice());
        assertEquals(false, response.getOnlyForLocalConsumption());
        assertEquals("/images/hamburguer.jpg", response.getImagePath());
        assertEquals(true, response.getIsActive());
        assertEquals(restaurantId, response.getRestaurantId());
        assertEquals(menuId, response.getCategoryId());
        assertNotNull(response.getId());

        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).save(any(RestaurantEntity.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenRestaurantNotExistsForCreateItem() {
        // Given
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> {
            menuItemUseCase.createMenuItem(restaurantId, menuId, menuItemRequest);
        });

        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenMenuCategoryNotExistsForCreateItem() {
        // Given
        String nonExistentMenuId = UUID.randomUUID().toString();
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));

        // When & Then
        assertThrows(NotFoundException.class, () -> {
            menuItemUseCase.createMenuItem(restaurantId, nonExistentMenuId, menuItemRequest);
        });

        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void shouldUpdateMenuItemSuccessfully() {
        // Given
        MenuItemRequest updateRequest = MenuItemRequest.builder()
            .name("Hambúrguer Premium")
            .description("Hambúrguer premium com ingredientes especiais")
            .price(new BigDecimal("35.90"))
            .onlyForLocalConsumption(true)
            .imagePath("/images/hamburguer-premium.jpg")
            .isActive(true)
            .build();

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);

        // When
        MenuItemResponse response = menuItemUseCase.updateMenuItem(restaurantId, menuId, itemId, updateRequest);

        // Then
        assertNotNull(response);
        assertEquals("Hambúrguer Premium", response.getName());
        assertEquals("Hambúrguer premium com ingredientes especiais", response.getDescription());
        assertEquals(new BigDecimal("35.90"), response.getPrice());
        assertEquals(true, response.getOnlyForLocalConsumption());
        assertEquals("/images/hamburguer-premium.jpg", response.getImagePath());
        assertEquals(itemId, response.getId());

        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).save(any(RestaurantEntity.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenMenuItemNotExistsForUpdate() {
        // Given
        String nonExistentItemId = UUID.randomUUID().toString();
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));

        // When & Then
        assertThrows(NotFoundException.class, () -> {
            menuItemUseCase.updateMenuItem(restaurantId, menuId, nonExistentItemId, menuItemRequest);
        });

        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void shouldDeleteMenuItemSuccessfully() {
        // Given
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);

        // When
        menuItemUseCase.deleteMenuItem(restaurantId, menuId, itemId);

        // Then
        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).save(any(RestaurantEntity.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenMenuItemNotExistsForDelete() {
        // Given
        String nonExistentItemId = UUID.randomUUID().toString();
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));

        // When & Then
        assertThrows(NotFoundException.class, () -> {
            menuItemUseCase.deleteMenuItem(restaurantId, menuId, nonExistentItemId);
        });

        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void shouldGetMenuItemByIdSuccessfully() {
        // Given
        when(restaurantRepository.findByMenuItemId(itemId)).thenReturn(Optional.of(restaurantEntity));

        // When
        MenuItemWithContextDTO response = menuItemUseCase.getMenuItemById(itemId);

        // Then
        assertNotNull(response);
        assertEquals(itemId, response.getId());
        assertEquals("X-Bacon", response.getName());
        assertEquals("Hambúrguer com bacon", response.getDescription());
        assertEquals(new BigDecimal("22.50"), response.getPrice());
        
        assertNotNull(response.getCategory());
        assertEquals(menuId, response.getCategory().getId());
        assertEquals("Lanche", response.getCategory().getType());
        
        assertNotNull(response.getRestaurant());
        assertEquals(restaurantId, response.getRestaurant().getId());
        assertEquals("Burger House", response.getRestaurant().getName());
        assertEquals("Rua dos Pinheiros, 789", response.getRestaurant().getAddress());

        verify(restaurantRepository).findByMenuItemId(itemId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenMenuItemNotExistsForGet() {
        // Given
        String nonExistentItemId = UUID.randomUUID().toString();
        when(restaurantRepository.findByMenuItemId(nonExistentItemId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> {
            menuItemUseCase.getMenuItemById(nonExistentItemId);
        });

        verify(restaurantRepository).findByMenuItemId(nonExistentItemId);
    }

    @Test
    void shouldCreateMenuItemWithDefaultValues() {
        // Given
        MenuItemRequest requestWithNulls = MenuItemRequest.builder()
            .name("Item Simples")
            .description("Descrição simples")
            .price(new BigDecimal("10.00"))
            .onlyForLocalConsumption(null) // Should default to false
            .imagePath(null)
            .isActive(null) // Should default to true
            .build();

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);

        // When
        MenuItemResponse response = menuItemUseCase.createMenuItem(restaurantId, menuId, requestWithNulls);

        // Then
        assertNotNull(response);
        assertEquals("Item Simples", response.getName());
        assertEquals(false, response.getOnlyForLocalConsumption()); // Default value
        assertEquals(true, response.getIsActive()); // Default value

        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).save(any(RestaurantEntity.class));
    }

    @Test
    void shouldCreateMenuItemInCategoryWithoutExistingItems() {
        // Given
        MenuCategoryEntity emptyCategory = MenuCategoryEntity.builder()
            .id(menuId)
            .type("Sobremesa")
            .items(null) // Sem itens existentes
            .build();

        RestaurantEntity restaurantWithEmptyCategory = RestaurantEntity.builder()
            .id(restaurantId)
            .name("Restaurante Teste")
            .address("Rua Teste, 123")
            .kitchenType(KitchenTypeEntity.builder().id(kitchenTypeId).name("Brasileira").build())
            .daysOperation(List.of(new OperationDaysTimeData(DayEnum.MONDAY, "08:00", "18:00")))
            .ownerId(ownerId)
            .isActive(true)
            .menu(List.of(emptyCategory))
            .lastUpdate(LocalDateTime.now())
            .createdAt(LocalDateTime.now())
            .build();

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantWithEmptyCategory));
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantWithEmptyCategory);

        // When
        MenuItemResponse response = menuItemUseCase.createMenuItem(restaurantId, menuId, menuItemRequest);

        // Then
        assertNotNull(response);
        assertEquals("Hambúrguer Artesanal", response.getName());
        assertEquals(restaurantId, response.getRestaurantId());
        assertEquals(menuId, response.getCategoryId());

        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).save(any(RestaurantEntity.class));
    }
}
