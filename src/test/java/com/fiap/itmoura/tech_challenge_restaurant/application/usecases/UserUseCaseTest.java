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

import com.fiap.itmoura.tech_challenge_restaurant.application.models.user.UserDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.UserRepository;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.UserEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.UserTypeEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.ConflictRequestException;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTypeUseCase userTypeUseCase;

    @InjectMocks
    private UserUseCase userUseCase;

    private UserDTO userDTO;
    private UserEntity userEntity;
    private UserTypeEntity userTypeEntity;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO("1", "João Silva", "joao@email.com", "userType1");
        userEntity = UserEntity.builder()
            .id("1")
            .name("João Silva")
            .email("joao@email.com")
            .userTypeId("userType1")
            .createdAt(LocalDateTime.now())
            .lastUpdate(LocalDateTime.now())
            .build();
        
        userTypeEntity = UserTypeEntity.builder()
            .id("userType1")
            .name("CLIENTE")
            .build();
    }

    @Test
    void shouldCreateUserSuccessfully() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userTypeUseCase.getUserTypeByIdOrName(anyString())).thenReturn(userTypeEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // When
        UserDTO result = userUseCase.createUser(userDTO);

        // Then
        assertNotNull(result);
        assertEquals("1", result.id());
        assertEquals("João Silva", result.name());
        assertEquals("joao@email.com", result.email());
        assertEquals("userType1", result.userTypeId());
        verify(userRepository).existsByEmail("joao@email.com");
        verify(userTypeUseCase).getUserTypeByIdOrName("userType1");
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void shouldThrowConflictWhenEmailAlreadyExists() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThrows(ConflictRequestException.class, 
            () -> userUseCase.createUser(userDTO));
        
        verify(userRepository).existsByEmail("joao@email.com");
        verify(userTypeUseCase, never()).getUserTypeByIdOrName(anyString());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void shouldGetUserByIdSuccessfully() {
        // Given
        when(userRepository.findById(anyString())).thenReturn(Optional.of(userEntity));

        // When
        UserEntity result = userUseCase.getUserById("1");

        // Then
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("João Silva", result.getName());
        verify(userRepository).findById("1");
    }

    @Test
    void shouldThrowNotFoundWhenUserNotExists() {
        // Given
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, 
            () -> userUseCase.getUserById("1"));
        
        verify(userRepository).findById("1");
    }

    @Test
    void shouldGetAllUsersSuccessfully() {
        // Given
        List<UserEntity> users = Arrays.asList(userEntity);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<UserDTO> result = userUseCase.getAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).id());
        assertEquals("João Silva", result.get(0).name());
        verify(userRepository).findAll();
    }

    @Test
    void shouldGetUsersByTypeSuccessfully() {
        // Given
        List<UserEntity> users = Arrays.asList(userEntity);
        when(userTypeUseCase.getUserTypeByIdOrName(anyString())).thenReturn(userTypeEntity);
        when(userRepository.findByUserTypeId(anyString())).thenReturn(users);

        // When
        List<UserDTO> result = userUseCase.getUsersByType("userType1");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("1", result.get(0).id());
        verify(userTypeUseCase).getUserTypeByIdOrName("userType1");
        verify(userRepository).findByUserTypeId("userType1");
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        // Given
        UserDTO updateDTO = new UserDTO("1", "João Santos", "joao.santos@email.com", "userType1");
        when(userRepository.findById(anyString())).thenReturn(Optional.of(userEntity));
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userTypeUseCase.getUserTypeByIdOrName(anyString())).thenReturn(userTypeEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        // When
        UserDTO result = userUseCase.updateUser("1", updateDTO);

        // Then
        assertNotNull(result);
        verify(userRepository).findById("1");
        verify(userRepository).existsByEmail("joao.santos@email.com");
        verify(userTypeUseCase).getUserTypeByIdOrName("userType1");
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        // Given
        when(userRepository.findById(anyString())).thenReturn(Optional.of(userEntity));

        // When
        userUseCase.deleteUser("1");

        // Then
        verify(userRepository).findById("1");
        verify(userRepository).delete(userEntity);
    }
}
