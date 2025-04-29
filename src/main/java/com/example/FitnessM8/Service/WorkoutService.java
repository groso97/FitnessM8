package com.example.FitnessM8.Service;

import com.example.FitnessM8.DTO.WorkoutDTO;
import com.example.FitnessM8.Mapper.WorkoutMapper;
import com.example.FitnessM8.Model.User;
import com.example.FitnessM8.Model.Workout;
import com.example.FitnessM8.Repository.UserRepository;
import com.example.FitnessM8.Repository.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutService {

    public final WorkoutRepository workoutRepository;
    public final UserRepository userRepository;
    public final WorkoutMapper workoutMapper;


    public WorkoutService(WorkoutRepository workoutRepository, UserRepository userRepository, WorkoutMapper workoutMapper) {
        this.workoutRepository = workoutRepository;
        this.userRepository = userRepository;
        this.workoutMapper = workoutMapper;
    }

    public void createWorkout(WorkoutDTO workoutDTO, String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new RuntimeException("User not found"));

        Workout workout = new Workout(
                workoutDTO.workoutName(),
                workoutDTO.exerciseType(),
                workoutDTO.durationMinutes(),
                workoutDTO.caloriesBurned(),
                user
        );

        workoutRepository.save(workout);
    }

    public List<WorkoutDTO> getWorkoutsForCurrentUser(String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return workoutRepository.findByUser(user)
                .stream()
                .map(workoutMapper::toDTO)
                .toList();
    }

    public Workout getWorkoutByIdAndUser(Long workoutId, User user) {
        return workoutRepository.findByWorkoutIdAndUser(workoutId, user)
                .orElseThrow(() -> new RuntimeException("Workout not found or not yours"));
    }


    public void deleteWorkoutById(Long workoutId, User user){
        Workout workout = workoutRepository.findById(workoutId)
                .orElseThrow(()->new RuntimeException("Workout not found"));

        if(!workout.getUser().getUserId().equals(user.getUserId())){
            throw new RuntimeException("You are not authorized to delete this workout");
        }

        workoutRepository.delete(workout);
    }

    public void updateWorkoutById(Long workoutId, WorkoutDTO workoutDTO, User user){
        Workout workout = workoutRepository.findByWorkoutIdAndUser(workoutId, user)
                .orElseThrow(() -> new RuntimeException("Workout not found or not yours"));


        workout.setWorkoutName(workoutDTO.workoutName());
        workout.setExerciseType(workoutDTO.exerciseType());
        workout.setDurationMinutes(workoutDTO.durationMinutes());
        workout.setCaloriesBurned(workoutDTO.caloriesBurned());

        workoutRepository.save(workout);

    }
}
