package com.example.FitnessM8.DTO;

import jakarta.validation.constraints.*;

public record MealDTO(
        Long mealId,
        @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Meal name can only contain letters and spaces.")
        @NotBlank(message = "Meal name cannot be empty.")
        @Size(min = 4, max = 50, message = "Meal name must be between 4 and 50 characters.")
        String mealName,
        @NotNull(message = "Calories value is required.")
        @Min(value = 10, message = "Calories must be at least 10 kcal.")
        @Max(value = 1500, message = "Calories must not exceed 1500 kcal.")
        Integer calories,
        @NotNull(message = "Protein amount is required.")
        @Min(value = 1, message = "Proteins must be at least 1g.")
        Integer proteins,
        @NotNull(message = "Fat amount is required.")
        @Min(value = 1, message = "Fats must be at least 1g.")
        Integer fats,
        @NotNull(message = "Carbohydrate amount is required.")
        @Min(value = 1, message = "Carbs must be at least 1g.")
        Integer carbs
) {
}
