package com.fiap.itmoura.tech_challenge_restaurant.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype.KitchenTypeDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.KitchenTypeRepository;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.KitchenTypeEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.ConflictRequestException;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
@DisplayName("KitchenTypeUseCase Tests")
class KitchenTypeUseCaseTest {

    @Mock
    private KitchenTypeRepository kitchenTypeRepository;

    @InjectMocks
    private KitchenTypeUseCase kitchenTypeUseCase;

    private KitchenTypeDTO kitchenTypeDTO;
    private KitchenTypeEntity kitchenTypeEntity;

    @BeforeEach
    void setUp() {
        kitchenTypeDTO = new KitchenTypeDTO("1", "Brasileira", "Cozinha brasileira tradicional");
        kitchenTypeEntity = KitchenTypeEntity.builder()
            .id("1")
            .name("BRASILEIRA")
            .description("Cozinha brasileira tradicional")
            .createdAt(LocalDateTime.now())
            .lastUpdate(LocalDateTime.now())
            .build();
    }

    @Nested
    @DisplayName("Create Kitchen Type Tests")
    class CreateKitchenTypeTests {

        @Test
        @DisplayName("Should create kitchen type successfully")
        void shouldCreateKitchenTypeSuccessfully() {
            // Given
            when(kitchenTypeRepository.existsByName(anyString())).thenReturn(false);
            when(kitchenTypeRepository.save(any(KitchenTypeEntity.class))).thenReturn(kitchenTypeEntity);

            // When
            KitchenTypeDTO result = kitchenTypeUseCase.createKitchenType(kitchenTypeDTO);

            // Then
            assertNotNull(result);
            assertEquals("1", result.id());
            assertEquals("BRASILEIRA", result.name());
            assertEquals("Cozinha brasileira tradicional", result.description());
            
            verify(kitchenTypeRepository).existsByName("Brasileira");
            verify(kitchenTypeRepository).save(any(KitchenTypeEntity.class));
        }

        @Test
        @DisplayName("Should throw ConflictRequestException when kitchen type already exists")
        void shouldThrowConflictWhenKitchenTypeAlreadyExists() {
            // Given
            when(kitchenTypeRepository.existsByName(anyString())).thenReturn(true);

            // When & Then
            ConflictRequestException exception = assertThrows(ConflictRequestException.class, 
                () -> kitchenTypeUseCase.createKitchenType(kitchenTypeDTO));
            
            assertEquals("Kitchen type already exists", exception.getMessage());
            verify(kitchenTypeRepository).existsByName("Brasileira");
            verify(kitchenTypeRepository, never()).save(any(KitchenTypeEntity.class));
        }

        @Test
        @DisplayName("Should convert name to uppercase when creating")
        void shouldConvertNameToUppercaseWhenCreating() {
            // Given
            KitchenTypeDTO lowerCaseDTO = new KitchenTypeDTO(null, "italiana", "Cozinha italiana");
            when(kitchenTypeRepository.existsByName(anyString())).thenReturn(false);
            when(kitchenTypeRepository.save(any(KitchenTypeEntity.class))).thenAnswer(invocation -> {
                KitchenTypeEntity entity = invocation.getArgument(0);
                entity.setId("2");
                return entity;
            });

            // When
            kitchenTypeUseCase.createKitchenType(lowerCaseDTO);

            // Then
            verify(kitchenTypeRepository).save(argThat(entity -> 
                "ITALIANA".equals(entity.getName())
            ));
        }
    }

    @Nested
    @DisplayName("Get Kitchen Type Tests")
    class GetKitchenTypeTests {

        @Test
        @DisplayName("Should get kitchen type by ID successfully")
        void shouldGetKitchenTypeByIdSuccessfully() {
            // Given
            when(kitchenTypeRepository.findByIdOrName(anyString())).thenReturn(Optional.of(kitchenTypeEntity));

            // When
            KitchenTypeEntity result = kitchenTypeUseCase.getKitchenTypeByIdOrName("1");

            // Then
            assertNotNull(result);
            assertEquals("1", result.getId());
            assertEquals("BRASILEIRA", result.getName());
            verify(kitchenTypeRepository).findByIdOrName("1");
        }

        @Test
        @DisplayName("Should get kitchen type by name successfully")
        void shouldGetKitchenTypeByNameSuccessfully() {
            // Given
            when(kitchenTypeRepository.findByIdOrName(anyString())).thenReturn(Optional.of(kitchenTypeEntity));

            // When
            KitchenTypeEntity result = kitchenTypeUseCase.getKitchenTypeByIdOrName("BRASILEIRA");

            // Then
            assertNotNull(result);
            assertEquals("BRASILEIRA", result.getName());
            verify(kitchenTypeRepository).findByIdOrName("BRASILEIRA");
        }

        @Test
        @DisplayName("Should throw NotFoundException when kitchen type not exists")
        void shouldThrowNotFoundWhenKitchenTypeNotExists() {
            // Given
            when(kitchenTypeRepository.findByIdOrName(anyString())).thenReturn(Optional.empty());

            // When & Then
            NotFoundException exception = assertThrows(NotFoundException.class, 
                () -> kitchenTypeUseCase.getKitchenTypeByIdOrName("nonexistent"));
            
            assertEquals("Kitchen type not found", exception.getMessage());
            verify(kitchenTypeRepository).findByIdOrName("nonexistent");
        }
    }

    @Nested
    @DisplayName("Update Kitchen Type Tests")
    class UpdateKitchenTypeTests {

        @Test
        @DisplayName("Should update kitchen type successfully")
        void shouldUpdateKitchenTypeSuccessfully() {
            // Given
            KitchenTypeDTO updateDTO = new KitchenTypeDTO("1", "Italiana", "Cozinha italiana autêntica");
            KitchenTypeEntity updatedEntity = KitchenTypeEntity.builder()
                .id("1")
                .name("ITALIANA")
                .description("Cozinha italiana autêntica")
                .build();
            
            when(kitchenTypeRepository.findByIdOrName(anyString())).thenReturn(Optional.of(kitchenTypeEntity));
            when(kitchenTypeRepository.save(any(KitchenTypeEntity.class))).thenReturn(updatedEntity);

            // When
            KitchenTypeDTO result = kitchenTypeUseCase.updateKitchenType("1", updateDTO);

            // Then
            assertNotNull(result);
            assertEquals("ITALIANA", result.name());
            verify(kitchenTypeRepository).findByIdOrName("1");
            verify(kitchenTypeRepository).save(any(KitchenTypeEntity.class));
        }

        @Test
        @DisplayName("Should throw NotFoundException when updating non-existent kitchen type")
        void shouldThrowNotFoundWhenUpdatingNonExistentKitchenType() {
            // Given
            KitchenTypeDTO updateDTO = new KitchenTypeDTO("1", "Italiana", "Cozinha italiana");
            when(kitchenTypeRepository.findByIdOrName(anyString())).thenReturn(Optional.empty());

            // When & Then
            NotFoundException exception = assertThrows(NotFoundException.class, 
                () -> kitchenTypeUseCase.updateKitchenType("1", updateDTO));
            
            assertEquals("Kitchen type not found", exception.getMessage());
            verify(kitchenTypeRepository).findByIdOrName("1");
            verify(kitchenTypeRepository, never()).save(any(KitchenTypeEntity.class));
        }
    }

    @Nested
    @DisplayName("Delete Kitchen Type Tests")
    class DeleteKitchenTypeTests {

        @Test
        @DisplayName("Should delete kitchen type successfully")
        void shouldDeleteKitchenTypeSuccessfully() {
            // Given
            when(kitchenTypeRepository.findByIdOrName(anyString())).thenReturn(Optional.of(kitchenTypeEntity));

            // When
            kitchenTypeUseCase.deleteKitchenType("1");

            // Then
            verify(kitchenTypeRepository).findByIdOrName("1");
            verify(kitchenTypeRepository).delete(kitchenTypeEntity);
        }

        @Test
        @DisplayName("Should throw NotFoundException when deleting non-existent kitchen type")
        void shouldThrowNotFoundWhenDeletingNonExistentKitchenType() {
            // Given
            when(kitchenTypeRepository.findByIdOrName(anyString())).thenReturn(Optional.empty());

            // When & Then
            NotFoundException exception = assertThrows(NotFoundException.class, 
                () -> kitchenTypeUseCase.deleteKitchenType("1"));
            
            assertEquals("Kitchen type not found", exception.getMessage());
            verify(kitchenTypeRepository).findByIdOrName("1");
            verify(kitchenTypeRepository, never()).delete(any(KitchenTypeEntity.class));
        }
    }
}
