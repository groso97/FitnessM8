package com.example.FitnessM8.DTO;

import jakarta.validation.constraints.*;

public record WorkoutDTO(
        Long workoutId,
        @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Workout name can only contain letters and spaces.")
        @NotBlank(message = "Workout name cannot be empty.")
        @Size(min = 4, max = 50, message = "Workout name must be between 4 and 50 characters.")
        String workoutName,
        @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Exercise type can only contain letters and spaces.")
        @NotBlank(message = "Exercise type cannot be empty.")
        @Size(min = 4, max = 50, message = "Exercise type must be between 4 and 50 characters.")
        String exerciseType,
        @NotNull(message = "Duration is required.")
        @Min(value = 10, message = "Duration of workout must be at least 10 minutes.")
        Integer durationMinutes,
        @NotNull(message = "Calories burned is required.")
        @Min(value = 10, message = "Calories burned must be at least 10kcal.")
        Integer caloriesBurned
) {
}
