package com.fiap.itmoura.tech_challenge_restaurant.application.usecases;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.usertype.UserTypeDTO;
import com.fiap.itmoura.tech_challenge_restaurant.application.ports.out.UserTypeRepository;
import com.fiap.itmoura.tech_challenge_restaurant.domain.entities.UserTypeEntity;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.ConflictRequestException;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor    
public class UserTypeUseCase {

    private final UserTypeRepository userTypeRepository;

    @Transactional
    public UserTypeDTO createUserType(UserTypeDTO userTypeDTO) {
        log.info("Creating user type: {}", userTypeDTO);

        if (userTypeRepository.existsByName(userTypeDTO.name())) {
            throw new ConflictRequestException("User type already exists");
        }

        UserTypeEntity userTypeEntity = userTypeDTO.toEntity();
        userTypeEntity.setName(userTypeDTO.name().toUpperCase());
        userTypeEntity.setLastUpdate(LocalDateTime.now());
        userTypeEntity.setCreatedAt(LocalDateTime.now());

        var userTypeSaved = userTypeRepository.save(userTypeEntity);

        return UserTypeDTO.fromEntity(userTypeSaved);
    }

    public UserTypeEntity getUserTypeByIdOrName(String idOrName) {
        return userTypeRepository.findByIdOrName(idOrName)
        .orElseThrow(() -> new NotFoundException("User type not found"));
    }

    public List<UserTypeDTO> getAllUserTypes() {
        log.info("Getting all user types");
        return userTypeRepository.findAll()
            .stream()
            .map(UserTypeDTO::fromEntity)
            .toList();
    }

    @Transactional
    public UserTypeDTO updateUserType(String id, UserTypeDTO userTypeDTO) {
        UserTypeEntity userTypeEntity = getUserTypeByIdOrName(id);
        
        // Verificar se já existe outro tipo com o mesmo nome
        if (!userTypeEntity.getName().equalsIgnoreCase(userTypeDTO.name()) && 
            userTypeRepository.existsByName(userTypeDTO.name())) {
            throw new ConflictRequestException("User type with this name already exists");
        }
        
        userTypeEntity.setName(userTypeDTO.name().toUpperCase());
        userTypeEntity.setLastUpdate(LocalDateTime.now());

        var userTypeSaved = userTypeRepository.save(userTypeEntity);

        return UserTypeDTO.fromEntity(userTypeSaved);
    }

    @Transactional
    public void deleteUserType(String id) {
        UserTypeEntity userTypeEntity = getUserTypeByIdOrName(id);
        userTypeRepository.delete(userTypeEntity);
    }
}
