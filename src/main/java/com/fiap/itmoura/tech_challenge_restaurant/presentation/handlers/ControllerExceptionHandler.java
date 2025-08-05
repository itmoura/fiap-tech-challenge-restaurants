package com.fiap.itmoura.tech_challenge_restaurant.presentation.handlers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.validationError.ValidationErrorDTO;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.BadRequestException;
import com.fiap.itmoura.tech_challenge_restaurant.domain.exceptions.ConflictRequestException;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDTO> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        logger.error("Validation error occurred: {}", ex.getMessage());
        
        var status = HttpStatus.BAD_REQUEST;
        List<String> errors = new ArrayList<>();
        for (var error : ex.getBindingResult().getFieldErrors()) {
            String errorMessage = error.getField() + ": " + error.getDefaultMessage();
            errors.add(errorMessage);
            logger.error("Field error: {}", errorMessage);
        }

        logger.error("Validation errors: {}", errors);

        return ResponseEntity
                .status(status.value())
                .body(new ValidationErrorDTO(errors, status.value()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ValidationErrorDTO> handlerBadRequestException(BadRequestException ex) {
        logger.error("Bad request error: {}", ex.getMessage());
        
        var status = HttpStatus.BAD_REQUEST;
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        return ResponseEntity
                .status(status.value())
                .body(new ValidationErrorDTO(errors, status.value()));
    }

    @ExceptionHandler(ConflictRequestException.class)
    public ResponseEntity<ValidationErrorDTO> handlerConflictRequestException(ConflictRequestException ex) {
        logger.error("Conflict error: {}", ex.getMessage());
        
        var status = HttpStatus.CONFLICT;
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        return ResponseEntity
                .status(status.value())
                .body(new ValidationErrorDTO(errors, status.value()));
    }
}
