package com.fiap.itmoura.tech_challenge_restaurant.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

import com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype.KitchenTypeRequest;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype.KitchenTypeResponse;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.KitchenTypeRepository;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.RestaurantRepository;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.KitchenTypeDocumentEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.KitchenTypeEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.RestaurantEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.ConflictRequestException;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
class KitchenTypeUseCaseTest {

    @Mock
    private KitchenTypeRepository kitchenTypeRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private KitchenTypeUseCase kitchenTypeUseCase;

    private KitchenTypeRequest kitchenTypeRequest;
    private KitchenTypeDocumentEntity kitchenTypeEntity;
    private String kitchenTypeId;

    @BeforeEach
    void setUp() {
        kitchenTypeId = UUID.randomUUID().toString();

        kitchenTypeRequest = KitchenTypeRequest.builder()
            .name("Italiana")
            .description("Cozinha italiana tradicional")
            .build();

        kitchenTypeEntity = KitchenTypeDocumentEntity.builder()
            .id(kitchenTypeId)
            .name("Italiana")
            .description("Cozinha italiana tradicional")
            .createdAt(LocalDateTime.now())
            .lastUpdate(LocalDateTime.now())
            .build();
    }

    @Test
    void shouldCreateKitchenTypeSuccessfully() {
        // Given
        when(kitchenTypeRepository.existsByNameIgnoreCase("Italiana")).thenReturn(false);
        when(kitchenTypeRepository.save(any(KitchenTypeDocumentEntity.class))).thenReturn(kitchenTypeEntity);

        // When
        KitchenTypeResponse response = kitchenTypeUseCase.createKitchenType(kitchenTypeRequest);

        // Then
        assertNotNull(response);
        assertEquals("Italiana", response.getName());
        assertEquals("Cozinha italiana tradicional", response.getDescription());
        assertEquals(kitchenTypeId, response.getId());

        verify(kitchenTypeRepository).existsByNameIgnoreCase("Italiana");
        verify(kitchenTypeRepository).save(any(KitchenTypeDocumentEntity.class));
    }

    @Test
    void shouldThrowConflictExceptionWhenKitchenTypeNameAlreadyExists() {
        // Given
        when(kitchenTypeRepository.existsByNameIgnoreCase("Italiana")).thenReturn(true);

        // When & Then
        ConflictRequestException exception = assertThrows(ConflictRequestException.class, () -> {
            kitchenTypeUseCase.createKitchenType(kitchenTypeRequest);
        });

        assertEquals("Kitchen type with name 'Italiana' already exists", exception.getMessage());
        verify(kitchenTypeRepository).existsByNameIgnoreCase("Italiana");
        verify(kitchenTypeRepository, never()).save(any());
    }

    @Test
    void shouldGetAllKitchenTypesSuccessfully() {
        // Given
        List<KitchenTypeDocumentEntity> entities = List.of(kitchenTypeEntity);
        when(kitchenTypeRepository.findAll()).thenReturn(entities);

        // When
        List<KitchenTypeResponse> response = kitchenTypeUseCase.getAllKitchenTypes();

        // Then
        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Italiana", response.get(0).getName());

        verify(kitchenTypeRepository).findAll();
    }

    @Test
    void shouldGetKitchenTypeByIdSuccessfully() {
        // Given
        when(kitchenTypeRepository.findById(kitchenTypeId)).thenReturn(Optional.of(kitchenTypeEntity));

        // When
        KitchenTypeResponse response = kitchenTypeUseCase.getKitchenTypeById(kitchenTypeId);

        // Then
        assertNotNull(response);
        assertEquals(kitchenTypeId, response.getId());
        assertEquals("Italiana", response.getName());

        verify(kitchenTypeRepository).findById(kitchenTypeId);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenKitchenTypeNotExists() {
        // Given
        when(kitchenTypeRepository.findById(kitchenTypeId)).thenReturn(Optional.empty());

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            kitchenTypeUseCase.getKitchenTypeById(kitchenTypeId);
        });

        assertEquals("Kitchen type not found with id: " + kitchenTypeId, exception.getMessage());
        verify(kitchenTypeRepository).findById(kitchenTypeId);
    }

    @Test
    void shouldUpdateKitchenTypeSuccessfully() {
        // Given
        KitchenTypeRequest updateRequest = KitchenTypeRequest.builder()
            .name("Italiana Premium")
            .description("Cozinha italiana premium")
            .build();

        when(kitchenTypeRepository.findById(kitchenTypeId)).thenReturn(Optional.of(kitchenTypeEntity));
        when(kitchenTypeRepository.existsByNameIgnoreCaseAndIdNot("Italiana Premium", kitchenTypeId)).thenReturn(false);
        when(kitchenTypeRepository.save(any(KitchenTypeDocumentEntity.class))).thenReturn(kitchenTypeEntity);

        // When
        KitchenTypeResponse response = kitchenTypeUseCase.updateKitchenType(kitchenTypeId, updateRequest);

        // Then
        assertNotNull(response);
        verify(kitchenTypeRepository).findById(kitchenTypeId);
        verify(kitchenTypeRepository).existsByNameIgnoreCaseAndIdNot("Italiana Premium", kitchenTypeId);
        verify(kitchenTypeRepository).save(any(KitchenTypeDocumentEntity.class));
    }

    @Test
    void shouldThrowConflictExceptionWhenUpdatingToExistingName() {
        // Given
        KitchenTypeRequest updateRequest = KitchenTypeRequest.builder()
            .name("Japonesa")
            .description("Cozinha japonesa")
            .build();

        when(kitchenTypeRepository.findById(kitchenTypeId)).thenReturn(Optional.of(kitchenTypeEntity));
        when(kitchenTypeRepository.existsByNameIgnoreCaseAndIdNot("Japonesa", kitchenTypeId)).thenReturn(true);

        // When & Then
        ConflictRequestException exception = assertThrows(ConflictRequestException.class, () -> {
            kitchenTypeUseCase.updateKitchenType(kitchenTypeId, updateRequest);
        });

        assertEquals("Kitchen type with name 'Japonesa' already exists", exception.getMessage());
        verify(kitchenTypeRepository).findById(kitchenTypeId);
        verify(kitchenTypeRepository).existsByNameIgnoreCaseAndIdNot("Japonesa", kitchenTypeId);
        verify(kitchenTypeRepository, never()).save(any());
    }

    @Test
    void shouldDeleteKitchenTypeSuccessfully() {
        KitchenTypeDocumentEntity kitchenType = KitchenTypeDocumentEntity.builder()
                .id(kitchenTypeId)
                .name("Italiana")
                .description("Cozinha italiana tradicional")
                .createdAt(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build();

        // Given
        when(kitchenTypeRepository.findById(kitchenTypeId)).thenReturn(Optional.of(kitchenType));

        // When
        kitchenTypeUseCase.deleteKitchenType(kitchenTypeId);

        // Then
        verify(kitchenTypeRepository).deleteById(kitchenTypeId);
    }

    @Test
    void shouldGetKitchenTypeByIdOrNameWithName() {
        // Given
        String name = "Italiana";
        when(kitchenTypeRepository.findByNameIgnoreCase(name)).thenReturn(Optional.of(kitchenTypeEntity));

        // When
        var result = kitchenTypeUseCase.getKitchenTypeByIdOrName(name);

        // Then
        assertNotNull(result);
        assertEquals("Italiana", result.getName());
        verify(kitchenTypeRepository).findByNameIgnoreCase(name);
    }

    @Test
    void shouldReturnNullWhenKitchenTypeNotFoundByIdOrName() {
        // Given
        String name = "Inexistente";
        when(kitchenTypeRepository.findByNameIgnoreCase(name)).thenReturn(Optional.empty());

        // Then
        assertThrows(NotFoundException.class, () -> {
            kitchenTypeUseCase.getKitchenTypeByIdOrName(name);
        });

        verify(kitchenTypeRepository).findByNameIgnoreCase(name);
    }
}
