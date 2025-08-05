package com.fiap.itmoura.tech_challenge_restaurant.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantRequest;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.restaurant.RestaurantResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.RestaurantRepository;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.DayEnum;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.KitchenTypeEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.OperationDaysTimeData;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.RestaurantEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("RestaurantUseCase Tests")
class RestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private KitchenTypeUseCase kitchenTypeUseCase;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private RestaurantRequest restaurantRequest;
    private RestaurantEntity restaurantEntity;
    private KitchenTypeEntity kitchenTypeEntity;
    private List<OperationDaysTimeData> operationDays;

    @BeforeEach
    void setUp() {
        operationDays = Arrays.asList(
            OperationDaysTimeData.builder()
                .day(DayEnum.MONDAY)
                .openingHours("08:00")
                .closingHours("22:00")
                .build()
        );

        kitchenTypeEntity = KitchenTypeEntity.builder()
            .id("1")
            .name("BRASILEIRA")
            .description("Cozinha brasileira")
            .build();

        restaurantRequest = new RestaurantRequest(
            "Restaurante do João",
            "Brasileira",
            "Rua das Flores, 123",
            operationDays,
            "owner1",
            true,
            4.5
        );

        restaurantEntity = RestaurantEntity.builder()
            .id("1")
            .name("Restaurante do João")
            .address("Rua das Flores, 123")
            .kitchenType(kitchenTypeEntity)
            .daysOperation(operationDays)
            .ownerId("owner1")
            .isActive(true)
            .rating(4.5)
            .createdAt(LocalDateTime.now())
            .lastUpdate(LocalDateTime.now())
            .build();
    }

    @Nested
    @DisplayName("Create Restaurant Tests")
    class CreateRestaurantTests {

        @Test
        @DisplayName("Should create restaurant successfully")
        void shouldCreateRestaurantSuccessfully() {
            // Given
            when(kitchenTypeUseCase.getKitchenTypeByIdOrName(anyString())).thenReturn(kitchenTypeEntity);
            when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);

            // When
            RestaurantResponse result = restaurantUseCase.createRestaurant(restaurantRequest);

            // Then
            assertNotNull(result);
            assertEquals("1", result.id());
            assertEquals("Restaurante do João", result.name());
            assertEquals("Rua das Flores, 123", result.address());
            assertTrue(result.isActive());
            assertEquals(4.5, result.rating());
            
            verify(kitchenTypeUseCase).getKitchenTypeByIdOrName("BRASILEIRA");
            verify(restaurantRepository).save(any(RestaurantEntity.class));
        }

        @Test
        @DisplayName("Should throw NotFoundException when kitchen type not exists")
        void shouldThrowNotFoundWhenKitchenTypeNotExists() {
            // Given
            when(kitchenTypeUseCase.getKitchenTypeByIdOrName(anyString())).thenReturn(null);

            // When & Then
            NotFoundException exception = assertThrows(NotFoundException.class, 
                () -> restaurantUseCase.createRestaurant(restaurantRequest));
            
            assertEquals("Kitchen type not found", exception.getMessage());
            verify(kitchenTypeUseCase).getKitchenTypeByIdOrName("BRASILEIRA");
            verify(restaurantRepository, never()).save(any(RestaurantEntity.class));
        }

        @Test
        @DisplayName("Should set default active status when not provided")
        void shouldSetDefaultActiveStatusWhenNotProvided() {
            // Given
            RestaurantRequest requestWithoutStatus = new RestaurantRequest(
                "Restaurante Teste",
                "Brasileira",
                "Rua Teste",
                operationDays,
                "owner1",
                null,
                4.0
            );

            when(kitchenTypeUseCase.getKitchenTypeByIdOrName(anyString())).thenReturn(kitchenTypeEntity);
            when(restaurantRepository.save(any(RestaurantEntity.class))).thenAnswer(invocation -> {
                RestaurantEntity entity = invocation.getArgument(0);
                entity.setId("2");
                return entity;
            });

            // When
            restaurantUseCase.createRestaurant(requestWithoutStatus);

            // Then
            verify(restaurantRepository).save(argThat(entity -> entity.getIsActive()));
        }
    }

    @Nested
    @DisplayName("Get Restaurant Tests")
    class GetRestaurantTests {

        @Test
        @DisplayName("Should get restaurant by ID successfully")
        void shouldGetRestaurantByIdSuccessfully() {
            // Given
            when(restaurantRepository.findById(anyString())).thenReturn(Optional.of(restaurantEntity));

            // When
            RestaurantResponse result = restaurantUseCase.getRestaurantById("1");

            // Then
            assertNotNull(result);
            assertEquals("1", result.id());
            assertEquals("Restaurante do João", result.name());
            verify(restaurantRepository).findById("1");
        }

        @Test
        @DisplayName("Should throw NotFoundException when restaurant not exists")
        void shouldThrowNotFoundWhenRestaurantNotExists() {
            // Given
            when(restaurantRepository.findById(anyString())).thenReturn(Optional.empty());

            // When & Then
            NotFoundException exception = assertThrows(NotFoundException.class, 
                () -> restaurantUseCase.getRestaurantById("nonexistent"));
            
            assertEquals("Restaurant not found", exception.getMessage());
            verify(restaurantRepository).findById("nonexistent");
        }

        @Test
        @DisplayName("Should get all active restaurants successfully")
        void shouldGetAllActiveRestaurantsSuccessfully() {
            // Given
            List<RestaurantEntity> restaurants = Arrays.asList(restaurantEntity);
            when(restaurantRepository.findByIsActiveTrue()).thenReturn(restaurants);

            // When
            List<RestaurantResponse> result = restaurantUseCase.getAllRestaurants();

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("1", result.get(0).id());
            assertTrue(result.get(0).isActive());
            verify(restaurantRepository).findByIsActiveTrue();
        }
    }

    @Nested
    @DisplayName("Update Restaurant Tests")
    class UpdateRestaurantTests {

        @Test
        @DisplayName("Should update restaurant successfully")
        void shouldUpdateRestaurantSuccessfully() {
            // Given
            RestaurantRequest updateRequest = new RestaurantRequest(
                "Restaurante Atualizado",
                "Italiana",
                "Nova Rua, 456",
                operationDays,
                "owner2",
                true,
                5.0
            );

            KitchenTypeEntity newKitchenType = KitchenTypeEntity.builder()
                .id("2")
                .name("ITALIANA")
                .build();

            when(restaurantRepository.findById(anyString())).thenReturn(Optional.of(restaurantEntity));
            when(kitchenTypeUseCase.getKitchenTypeByIdOrName("ITALIANA")).thenReturn(newKitchenType);
            when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);

            // When
            RestaurantResponse result = restaurantUseCase.updateRestaurant("1", updateRequest);

            // Then
            assertNotNull(result);
            verify(restaurantRepository).findById("1");
            verify(kitchenTypeUseCase, atLeastOnce()).getKitchenTypeByIdOrName("ITALIANA");
            verify(restaurantRepository).save(any(RestaurantEntity.class));
        }

        @Test
        @DisplayName("Should throw NotFoundException when updating non-existent restaurant")
        void shouldThrowNotFoundWhenUpdatingNonExistentRestaurant() {
            // Given
            when(restaurantRepository.findById(anyString())).thenReturn(Optional.empty());

            // When & Then
            NotFoundException exception = assertThrows(NotFoundException.class, 
                () -> restaurantUseCase.updateRestaurant("1", restaurantRequest));
            
            assertEquals("Restaurant not found", exception.getMessage());
            verify(restaurantRepository).findById("1");
            verify(restaurantRepository, never()).save(any(RestaurantEntity.class));
        }
    }

    @Nested
    @DisplayName("Delete Restaurant Tests")
    class DeleteRestaurantTests {

        @Test
        @DisplayName("Should delete restaurant successfully")
        void shouldDeleteRestaurantSuccessfully() {
            // Given
            when(restaurantRepository.findById(anyString())).thenReturn(Optional.of(restaurantEntity));

            // When
            restaurantUseCase.deleteRestaurant("1");

            // Then
            verify(restaurantRepository).findById("1");
            verify(restaurantRepository).delete(restaurantEntity);
        }

        @Test
        @DisplayName("Should throw NotFoundException when deleting non-existent restaurant")
        void shouldThrowNotFoundWhenDeletingNonExistentRestaurant() {
            // Given
            when(restaurantRepository.findById(anyString())).thenReturn(Optional.empty());

            // When & Then
            NotFoundException exception = assertThrows(NotFoundException.class, 
                () -> restaurantUseCase.deleteRestaurant("1"));
            
            assertEquals("Restaurant not found", exception.getMessage());
            verify(restaurantRepository).findById("1");
            verify(restaurantRepository, never()).delete(any(RestaurantEntity.class));
        }
    }

    @Nested
    @DisplayName("Disable Restaurant Tests")
    class DisableRestaurantTests {

        @Test
        @DisplayName("Should disable restaurant successfully")
        void shouldDisableRestaurantSuccessfully() {
            // Given
            when(restaurantRepository.findById(anyString())).thenReturn(Optional.of(restaurantEntity));
            when(restaurantRepository.save(any(RestaurantEntity.class))).thenReturn(restaurantEntity);

            // When
            restaurantUseCase.disableRestaurant("1");

            // Then
            verify(restaurantRepository).findById("1");
            verify(restaurantRepository).save(argThat(entity -> !entity.getIsActive()));
        }

        @Test
        @DisplayName("Should throw NotFoundException when disabling non-existent restaurant")
        void shouldThrowNotFoundWhenDisablingNonExistentRestaurant() {
            // Given
            when(restaurantRepository.findById(anyString())).thenReturn(Optional.empty());

            // When & Then
            NotFoundException exception = assertThrows(NotFoundException.class, 
                () -> restaurantUseCase.disableRestaurant("1"));
            
            assertEquals("Restaurant not found", exception.getMessage());
            verify(restaurantRepository).findById("1");
            verify(restaurantRepository, never()).save(any(RestaurantEntity.class));
        }
    }
}
