package com.fiap.itmoura.tech_challenge_restaurant.model.data;

import com.fiap.itmoura.tech_challenge_restaurant.model.enums.DayEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperationDaysTimeData {

    @Schema(title = "Dia", description = "Dia da semana", example = "Segunda-feira")
    private DayEnum day;

    @Schema(title = "Horário de abertura", description = "Horário de abertura do restaurante", example = "08:00")
    private String openingHours;

    @Schema(title = "Horário de fechamento", description = "Horário de fechamento do restaurante", example = "18:00")
    private String closingHours;
}
