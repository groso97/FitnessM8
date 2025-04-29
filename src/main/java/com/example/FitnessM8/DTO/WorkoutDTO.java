package com.example.FitnessM8.DTO;

public record WorkoutDTO(
        Long workoutId,
        String workoutName,
        String exerciseType,
        int durationMinutes,
        int caloriesBurned
) {
}
