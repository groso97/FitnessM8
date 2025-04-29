package com.example.FitnessM8.Mapper;

import com.example.FitnessM8.DTO.WorkoutDTO;
import com.example.FitnessM8.Model.Workout;
import org.springframework.stereotype.Service;

@Service
public class WorkoutMapper {

    public WorkoutDTO toDTO(Workout workout){
        return new WorkoutDTO(
                workout.getWorkoutId(),
                workout.getWorkoutName(),
                workout.getExerciseType(),
                workout.getDurationMinutes(),
                workout.getCaloriesBurned()
        );
    }

    public Workout toEntity(WorkoutDTO workoutDTO){
        Workout workout = new Workout();

        workout.setWorkoutName(workoutDTO.workoutName());
        workout.setExerciseType(workoutDTO.exerciseType());
        workout.setDurationMinutes(workoutDTO.durationMinutes());
        workout.setCaloriesBurned(workoutDTO.caloriesBurned());

        return workout;
    }
}
