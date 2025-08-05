package com.fiap.itmoura.tech_challenge_restaurant.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuCategoryRequest;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuCategoryResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.RestaurantRepository;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.DayEnum;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.KitchenTypeEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.MenuCategoryEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.OperationDaysTimeData;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.RestaurantEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
class MenuUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private MenuUseCase menuUseCase;

    private RestaurantEntity restaurantEntity;
    private MenuCategoryRequest menuCategoryRequest;
    private String restaurantId;
    private String menuId;
    private String kitchenTypeId;
    private String ownerId;

    @BeforeEach
    void setUp() {
        restaurantId = UUID.randomUUID().toString();
        menuId = UUID.randomUUID().toString();
        kitchenTypeId = UUID.randomUUID().toString();
        ownerId = UUID.randomUUID().toString();

        menuCategoryRequest = MenuCategoryRequest.builder()
            .type("Lanche")
            .build();

        KitchenTypeEntity kitchenTypeEntity = KitchenTypeEntity.builder()
            .id(kitchenTypeId)
            .name("Japonesa")
            .description("Cozinha Japonesa")
            .build();

        MenuCategoryEntity existingCategory = MenuCategoryEntity.builder()
            .id(menuId)
            .type("Bebida")
            .items(List.of())
            .build();

        restaurantEntity = RestaurantEntity.builder()
            .id(restaurantId)
            .name("Restaurante do João")
            .address("Rua das Flores, 123")
            .kitchenType(kitchenTypeEntity)
            .daysOperation(List.of(new OperationDaysTimeData(DayEnum.MONDAY, "08:00", "18:00")))
            .ownerId(ownerId)
            .isActive(true)
            .menu(List.of(existingCategory))
            .lastUpdate(LocalDateTime.now())
            .createdAt(LocalDateTime.now())
            .build();
    }

    @Test
    void shouldCreateMenuCategorySuccessfully() {
        // Given
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);

        // When
        MenuCategoryResponse response = menuUseCase.createMenuCategory(restaurantId, menuCategoryRequest);

        // Then
        assertNotNull(response);
        assertEquals("Lanche", response.getType());
        assertEquals(restaurantId, response.getRestaurantId());
        assertNotNull(response.getId());
        assertNotNull(response.getItems());

        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).save(any(RestaurantEntity.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenRestaurantNotExistsForCreate() {
        // Given
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> {
            menuUseCase.createMenuCategory(restaurantId, menuCategoryRequest);
        });

        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void shouldUpdateMenuCategorySuccessfully() {
        // Given
        MenuCategoryRequest updateRequest = MenuCategoryRequest.builder()
            .type("Lanche Atualizado")
            .build();

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);

        // When
        MenuCategoryResponse response = menuUseCase.updateMenuCategory(restaurantId, menuId, updateRequest);

        // Then
        assertNotNull(response);
        assertEquals("Lanche Atualizado", response.getType());
        assertEquals(restaurantId, response.getRestaurantId());
        assertEquals(menuId, response.getId());

        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).save(any(RestaurantEntity.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenMenuCategoryNotExistsForUpdate() {
        // Given
        String nonExistentMenuId = UUID.randomUUID().toString();
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));

        // When & Then
        assertThrows(NotFoundException.class, () -> {
            menuUseCase.updateMenuCategory(restaurantId, nonExistentMenuId, menuCategoryRequest);
        });

        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void shouldDeleteMenuCategorySuccessfully() {
        // Given
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);

        // When
        menuUseCase.deleteMenuCategory(restaurantId, menuId);

        // Then
        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).save(any(RestaurantEntity.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenMenuCategoryNotExistsForDelete() {
        // Given
        String nonExistentMenuId = UUID.randomUUID().toString();
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));

        // When & Then
        assertThrows(NotFoundException.class, () -> {
            menuUseCase.deleteMenuCategory(restaurantId, nonExistentMenuId);
        });

        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void shouldGetMenuCategorySuccessfully() {
        // Given
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));

        // When
        MenuCategoryResponse response = menuUseCase.getMenuCategory(restaurantId, menuId);

        // Then
        assertNotNull(response);
        assertEquals("Bebida", response.getType());
        assertEquals(restaurantId, response.getRestaurantId());
        assertEquals(menuId, response.getId());

        verify(restaurantRepository).findById(restaurantId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenRestaurantNotExistsForGet() {
        // Given
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> {
            menuUseCase.getMenuCategory(restaurantId, menuId);
        });

        verify(restaurantRepository).findById(restaurantId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenMenuCategoryNotExistsForGet() {
        // Given
        String nonExistentMenuId = UUID.randomUUID().toString();
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));

        // When & Then
        assertThrows(NotFoundException.class, () -> {
            menuUseCase.getMenuCategory(restaurantId, nonExistentMenuId);
        });

        verify(restaurantRepository).findById(restaurantId);
    }

    @Test
    void shouldCreateMenuCategoryInRestaurantWithoutExistingMenu() {
        // Given
        RestaurantEntity restaurantWithoutMenu = RestaurantEntity.builder()
            .id(restaurantId)
            .name("Restaurante Novo")
            .address("Rua Nova, 456")
            .kitchenType(KitchenTypeEntity.builder().id(kitchenTypeId).name("Italiana").build())
            .daysOperation(List.of(new OperationDaysTimeData(DayEnum.MONDAY, "08:00", "18:00")))
            .ownerId(ownerId)
            .isActive(true)
            .menu(null) // Sem menu existente
            .lastUpdate(LocalDateTime.now())
            .createdAt(LocalDateTime.now())
            .build();

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantWithoutMenu));
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantWithoutMenu);

        // When
        MenuCategoryResponse response = menuUseCase.createMenuCategory(restaurantId, menuCategoryRequest);

        // Then
        assertNotNull(response);
        assertEquals("Lanche", response.getType());
        assertEquals(restaurantId, response.getRestaurantId());

        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).save(any(RestaurantEntity.class));
    }
}
