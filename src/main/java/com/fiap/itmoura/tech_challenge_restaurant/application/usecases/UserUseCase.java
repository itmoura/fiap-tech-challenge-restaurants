package com.fiap.itmoura.tech_challenge_restaurant.application.usecases;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.user.UserDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.UserRepository;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.UserEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.ConflictRequestException;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor    
public class UserUseCase {

    private final UserRepository userRepository;
    private final UserTypeUseCase userTypeUseCase;

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        log.info("Creating user: {}", userDTO);

        if (userRepository.existsByEmail(userDTO.email())) {
            throw new ConflictRequestException("User with this email already exists");
        }

        // Verificar se o tipo de usuário existe
        userTypeUseCase.getUserTypeByIdOrName(userDTO.userTypeId());

        UserEntity userEntity = userDTO.toEntity();
        userEntity.setLastUpdate(LocalDateTime.now());
        userEntity.setCreatedAt(LocalDateTime.now());

        var userSaved = userRepository.save(userEntity);

        return UserDTO.fromEntity(userSaved);
    }

    public UserEntity getUserById(String id) {
        return userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public List<UserDTO> getAllUsers() {
        log.info("Getting all users");
        return userRepository.findAll()
            .stream()
            .map(UserDTO::fromEntity)
            .toList();
    }

    public List<UserDTO> getUsersByType(String userTypeId) {
        log.info("Getting users by type: {}", userTypeId);
        
        // Verificar se o tipo de usuário existe
        userTypeUseCase.getUserTypeByIdOrName(userTypeId);
        
        return userRepository.findByUserTypeId(userTypeId)
            .stream()
            .map(UserDTO::fromEntity)
            .toList();
    }

    @Transactional
    public UserDTO updateUser(String id, UserDTO userDTO) {
        UserEntity userEntity = getUserById(id);
        
        // Verificar se já existe outro usuário com o mesmo email
        if (!userEntity.getEmail().equalsIgnoreCase(userDTO.email()) && 
            userRepository.existsByEmail(userDTO.email())) {
            throw new ConflictRequestException("User with this email already exists");
        }

        // Verificar se o tipo de usuário existe
        userTypeUseCase.getUserTypeByIdOrName(userDTO.userTypeId());
        
        userEntity.setName(userDTO.name());
        userEntity.setEmail(userDTO.email());
        userEntity.setUserTypeId(userDTO.userTypeId());
        userEntity.setLastUpdate(LocalDateTime.now());

        var userSaved = userRepository.save(userEntity);

        return UserDTO.fromEntity(userSaved);
    }

    @Transactional
    public void deleteUser(String id) {
        UserEntity userEntity = getUserById(id);
        userRepository.delete(userEntity);
    }
}
