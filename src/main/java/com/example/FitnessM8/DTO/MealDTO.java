package com.example.FitnessM8.DTO;

public record MealDTO(
        Long mealId,
        String mealName,
        int calories,
        int proteins,
        int fats,
        int carbs
) {
}
