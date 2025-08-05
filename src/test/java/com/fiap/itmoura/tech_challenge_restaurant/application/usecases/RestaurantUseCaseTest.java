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

import com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype.KitchenTypeDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuCategoryDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.menu.MenuItemNestedDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantBasicResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantFullResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantRequest;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.RestaurantRepository;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.DayEnum;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.KitchenTypeEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.MenuCategoryEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.MenuItemEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.OperationDaysTimeData;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.RestaurantEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private RestaurantRequest restaurantRequest;
    private RestaurantEntity restaurantEntity;
    private UUID restaurantId;
    private UUID ownerId;
    private UUID kitchenTypeId;
    private UUID categoryId;
    private UUID itemId;

    @BeforeEach
    void setUp() {
        restaurantId = UUID.randomUUID();
        ownerId = UUID.randomUUID();
        kitchenTypeId = UUID.randomUUID();
        categoryId = UUID.randomUUID();
        itemId = UUID.randomUUID();

        KitchenTypeDTO kitchenTypeDTO = new KitchenTypeDTO(
            kitchenTypeId,
            "Japonesa",
            "Cozinha Japonesa"
        );

        MenuItemNestedDTO menuItem = MenuItemNestedDTO.builder()
            .id(itemId)
            .name("Hambúrguer Artesanal")
            .description("Hambúrguer com carne artesanal")
            .price(new BigDecimal("25.90"))
            .onlyForLocalConsumption(false)
            .imagePath("/images/hamburguer.jpg")
            .isActive(true)
            .build();

        MenuCategoryDTO menuCategory = MenuCategoryDTO.builder()
            .id(categoryId)
            .type("Lanche")
            .items(List.of(menuItem))
            .build();

        restaurantRequest = new RestaurantRequest(
            "Restaurante do João",
            "Rua das Flores, 123",
            kitchenTypeDTO,
            List.of(new OperationDaysTimeData(DayEnum.MONDAY, "08:00", "18:00")),
            ownerId,
            true,
            List.of(menuCategory)
        );

        KitchenTypeEntity kitchenTypeEntity = KitchenTypeEntity.builder()
            .id(kitchenTypeId)
            .name("Japonesa")
            .description("Cozinha Japonesa")
            .build();

        MenuItemEntity menuItemEntity = MenuItemEntity.builder()
            .id(itemId)
            .name("Hambúrguer Artesanal")
            .description("Hambúrguer com carne artesanal")
            .price(new BigDecimal("25.90"))
            .onlyForLocalConsumption(false)
            .imagePath("/images/hamburguer.jpg")
            .isActive(true)
            .build();

        MenuCategoryEntity menuCategoryEntity = MenuCategoryEntity.builder()
            .id(categoryId)
            .type("Lanche")
            .items(List.of(menuItemEntity))
            .build();

        restaurantEntity = RestaurantEntity.builder()
            .id(restaurantId)
            .name("Restaurante do João")
            .address("Rua das Flores, 123")
            .kitchenType(kitchenTypeEntity)
            .daysOperation(List.of(new OperationDaysTimeData(DayEnum.MONDAY, "08:00", "18:00")))
            .ownerId(ownerId)
            .isActive(true)
            .menu(List.of(menuCategoryEntity))
            .lastUpdate(LocalDateTime.now())
            .createdAt(LocalDateTime.now())
            .build();
    }

    @Test
    void shouldCreateRestaurantSuccessfully() {
        // Given
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);

        // When
        RestaurantFullResponse response = restaurantUseCase.createRestaurant(restaurantRequest);

        // Then
        assertNotNull(response);
        assertEquals("Restaurante do João", response.name());
        assertEquals("Rua das Flores, 123", response.address());
        assertEquals(ownerId, response.ownerId());
        assertTrue(response.isActive());
        assertNotNull(response.menu());
        assertEquals(1, response.menu().size());
        assertEquals("Lanche", response.menu().get(0).getType());

        verify(restaurantRepository).save(any(RestaurantEntity.class));
    }

    @Test
    void shouldGetAllRestaurantsWithoutMenu() {
        // Given
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurantEntity));

        // When
        List<RestaurantBasicResponse> response = restaurantUseCase.getAllRestaurants();

        // Then
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Restaurante do João", response.get(0).name());
        assertEquals("Rua das Flores, 123", response.get(0).address());

        verify(restaurantRepository).findAll();
    }

    @Test
    void shouldGetAllRestaurantsWithMenu() {
        // Given
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurantEntity));

        // When
        List<RestaurantFullResponse> response = restaurantUseCase.getAllRestaurantsWithMenu();

        // Then
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Restaurante do João", response.get(0).name());
        assertNotNull(response.get(0).menu());
        assertEquals(1, response.get(0).menu().size());

        verify(restaurantRepository).findAll();
    }

    @Test
    void shouldGetRestaurantByIdSuccessfully() {
        // Given
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));

        // When
        RestaurantFullResponse response = restaurantUseCase.getRestaurantById(restaurantId);

        // Then
        assertNotNull(response);
        assertEquals(restaurantId, response.id());
        assertEquals("Restaurante do João", response.name());

        verify(restaurantRepository).findById(restaurantId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenRestaurantNotExists() {
        // Given
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> {
            restaurantUseCase.getRestaurantById(restaurantId);
        });

        verify(restaurantRepository).findById(restaurantId);
    }

    @Test
    void shouldUpdateRestaurantSuccessfully() {
        // Given
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);

        // When
        RestaurantFullResponse response = restaurantUseCase.updateRestaurant(restaurantId, restaurantRequest);

        // Then
        assertNotNull(response);
        assertEquals("Restaurante do João", response.name());

        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).save(any(RestaurantEntity.class));
    }

    @Test
    void shouldDeleteRestaurantSuccessfully() {
        // Given
        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);

        // When
        restaurantUseCase.deleteRestaurant(restaurantId);

        // Then
        verify(restaurantRepository).existsById(restaurantId);
        verify(restaurantRepository).deleteById(restaurantId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeletingNonExistentRestaurant() {
        // Given
        when(restaurantRepository.existsById(restaurantId)).thenReturn(false);

        // When & Then
        assertThrows(NotFoundException.class, () -> {
            restaurantUseCase.deleteRestaurant(restaurantId);
        });

        verify(restaurantRepository).existsById(restaurantId);
        verify(restaurantRepository, never()).deleteById(any());
    }

    @Test
    void shouldCreateRestaurantWithoutMenu() {
        // Given
        RestaurantRequest requestWithoutMenu = new RestaurantRequest(
            "Restaurante Simples",
            "Rua Simples, 456",
            new KitchenTypeDTO(kitchenTypeId, "Italiana", "Cozinha Italiana"),
            List.of(new OperationDaysTimeData(DayEnum.TUESDAY, "09:00", "22:00")),
            ownerId,
            true,
            null // Sem menu
        );

        RestaurantEntity entityWithoutMenu = RestaurantEntity.builder()
            .id(restaurantId)
            .name("Restaurante Simples")
            .address("Rua Simples, 456")
            .kitchenType(KitchenTypeEntity.builder().id(kitchenTypeId).name("Italiana").build())
            .daysOperation(List.of(new OperationDaysTimeData(DayEnum.TUESDAY, "09:00", "22:00")))
            .ownerId(ownerId)
            .isActive(true)
            .menu(List.of())
            .lastUpdate(LocalDateTime.now())
            .createdAt(LocalDateTime.now())
            .build();

        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(entityWithoutMenu);

        // When
        RestaurantFullResponse response = restaurantUseCase.createRestaurant(requestWithoutMenu);

        // Then
        assertNotNull(response);
        assertEquals("Restaurante Simples", response.name());
        assertNotNull(response.menu());
        assertTrue(response.menu().isEmpty());

        verify(restaurantRepository).save(any(RestaurantEntity.class));
    }

    @Test
    void shouldGetAllActiveRestaurants() {
        // Given
        when(restaurantRepository.findByIsActiveTrue()).thenReturn(List.of(restaurantEntity));

        // When
        List<RestaurantBasicResponse> response = restaurantUseCase.getAllActiveRestaurants();

        // Then
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Restaurante do João", response.get(0).name());
        assertTrue(response.get(0).isActive());

        verify(restaurantRepository).findByIsActiveTrue();
    }

    @Test
    void shouldUpdateRestaurantWithNullIsActiveKeepingExistingValue() {
        // Given
        RestaurantRequest requestWithNullIsActive = new RestaurantRequest(
            "Restaurante Atualizado",
            "Rua Atualizada, 789",
            new KitchenTypeDTO(kitchenTypeId, "Brasileira", "Cozinha Brasileira"),
            List.of(new OperationDaysTimeData(DayEnum.WEDNESDAY, "10:00", "21:00")),
            ownerId,
            null, // isActive null - deve manter o valor existente
            null
        );

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurantEntity));
        when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);

        // When
        RestaurantFullResponse response = restaurantUseCase.updateRestaurant(restaurantId, requestWithNullIsActive);

        // Then
        assertNotNull(response);
        assertTrue(response.isActive()); // Deve manter o valor original (true)

        verify(restaurantRepository).findById(restaurantId);
        verify(restaurantRepository).save(any(RestaurantEntity.class));
    }
}
