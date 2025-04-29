package com.example.FitnessM8.DTO;

import java.time.LocalDate;

public record FitnessGoalDTO(
        Long fitnessGoalId,
        String title,
        String description,
        LocalDate targetDate,
        Boolean completed
) {
}
