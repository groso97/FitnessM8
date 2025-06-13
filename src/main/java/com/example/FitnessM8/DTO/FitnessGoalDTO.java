package com.example.FitnessM8.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record FitnessGoalDTO(
        Long fitnessGoalId,
        @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Fitness goal title can only contain letters and spaces.")
        @NotBlank(message = "Fitness goal title cannot be empty.")
        @Size(min = 4, max = 80, message = "Fitness goal title must be between 4 and 50 characters.")
        String title,
        String description,
        LocalDate targetDate,
        Boolean completed
) {
}
