package com.fiap.itmoura.tech_challenge_restaurant.application.models.validationError;

import java.util.List;

public record ValidationErrorDTO(List<String> errors, int status) {
}
