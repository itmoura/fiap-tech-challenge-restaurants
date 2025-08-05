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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.usertype.UserTypeDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.UserTypeRepository;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.UserTypeEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.ConflictRequestException;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
class UserTypeUseCaseTest {

    @Mock
    private UserTypeRepository userTypeRepository;

    @InjectMocks
    private UserTypeUseCase userTypeUseCase;

    private UserTypeDTO userTypeDTO;
    private UserTypeEntity userTypeEntity;

    @BeforeEach
    void setUp() {
        userTypeDTO = new UserTypeDTO("1", "Cliente");
        userTypeEntity = UserTypeEntity.builder()
            .id("1")
            .name("CLIENTE")
            .createdAt(LocalDateTime.now())
            .lastUpdate(LocalDateTime.now())
            .build();
    }

    @Test
    void shouldCreateUserTypeSuccessfully() {
        // Given
        when(userTypeRepository.existsByName(anyString())).thenReturn(false);
        when(userTypeRepository.save(any(UserTypeEntity.class))).thenReturn(userTypeEntity);

        // When
        UserTypeDTO result = userTypeUseCase.createUserType(userTypeDTO);

        // Then
        assertNotNull(result);
        assertEquals("1", result.id());
        assertEquals("CLIENTE", result.name());
        verify(userTypeRepository).existsByName("Cliente");
        verify(userTypeRepository).save(any(UserTypeEntity.class));
    }

    @Test
    void shouldThrowConflictWhenUserTypeAlreadyExists() {
        // Given
        when(userTypeRepository.existsByName(anyString())).thenReturn(true);

        // When & Then
        assertThrows(ConflictRequestException.class, 
            () -> userTypeUseCase.createUserType(userTypeDTO));
        
        verify(userTypeRepository).existsByName("Cliente");
        verify(userTypeRepository, never()).save(any(UserTypeEntity.class));
    }

    @Test
    void shouldGetUserTypeByIdOrNameSuccessfully() {
        // Given
        when(userTypeRepository.findByIdOrName(anyString())).thenReturn(Optional.of(userTypeEntity));

        // When
        UserTypeEntity result = userTypeUseCase.getUserTypeByIdOrName("1");

        // Then
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("CLIENTE", result.getName());
        verify(userTypeRepository).findByIdOrName("1");
    }

    @Test
    void shouldThrowNotFoundWhenUserTypeNotExists() {
        // Given
        when(userTypeRepository.findByIdOrName(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, 
            () -> userTypeUseCase.getUserTypeByIdOrName("1"));
        
        verify(userTypeRepository).findByIdOrName("1");
    }

    @Test
    void shouldGetAllUserTypesSuccessfully() {
        // Given
        List<UserTypeEntity> userTypes = Arrays.asList(userTypeEntity);
        when(userTypeRepository.findAll()).thenReturn(userTypes);

        // When
        List<UserTypeDTO> result = userTypeUseCase.getAllUserTypes();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).id());
        assertEquals("CLIENTE", result.get(0).name());
        verify(userTypeRepository).findAll();
    }

    @Test
    void shouldUpdateUserTypeSuccessfully() {
        // Given
        UserTypeDTO updateDTO = new UserTypeDTO("1", "Dono de Restaurante");
        when(userTypeRepository.findByIdOrName(anyString())).thenReturn(Optional.of(userTypeEntity));
        when(userTypeRepository.existsByName(anyString())).thenReturn(false);
        when(userTypeRepository.save(any(UserTypeEntity.class))).thenReturn(userTypeEntity);

        // When
        UserTypeDTO result = userTypeUseCase.updateUserType("1", updateDTO);

        // Then
        assertNotNull(result);
        verify(userTypeRepository).findByIdOrName("1");
        verify(userTypeRepository).existsByName("Dono de Restaurante");
        verify(userTypeRepository).save(any(UserTypeEntity.class));
    }

    @Test
    void shouldDeleteUserTypeSuccessfully() {
        // Given
        when(userTypeRepository.findByIdOrName(anyString())).thenReturn(Optional.of(userTypeEntity));

        // When
        userTypeUseCase.deleteUserType("1");

        // Then
        verify(userTypeRepository).findByIdOrName("1");
        verify(userTypeRepository).delete(userTypeEntity);
    }
}
