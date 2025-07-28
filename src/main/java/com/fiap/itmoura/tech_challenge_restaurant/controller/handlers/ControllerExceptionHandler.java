package com.fiap.itmoura.tech_challenge_restaurant.controller.handlers;

import com.fiap.itmoura.tech_challenge_restaurant.exception.BadRequestException;
import com.fiap.itmoura.tech_challenge_restaurant.exception.ConflictRequestException;
import com.fiap.itmoura.tech_challenge_restaurant.model.dto.ValidationErrorDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorDTO> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var status = HttpStatus.BAD_REQUEST;
        List<String> errors = new ArrayList<String>();
        for (var error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        return ResponseEntity
                .status(status.value())
                .body(new ValidationErrorDTO(errors, status.value()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ValidationErrorDTO> handlerBadRequestException(BadRequestException ex) {
        var status = HttpStatus.BAD_REQUEST;
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        return ResponseEntity
                .status(status.value())
                .body(new ValidationErrorDTO(errors, status.value()));
    }

    @ExceptionHandler(ConflictRequestException.class)
    public ResponseEntity<ValidationErrorDTO> handlerConflictRequestException(ConflictRequestException ex) {
        var status = HttpStatus.CONFLICT;
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        return ResponseEntity
                .status(status.value())
                .body(new ValidationErrorDTO(errors, status.value()));
    }
}
