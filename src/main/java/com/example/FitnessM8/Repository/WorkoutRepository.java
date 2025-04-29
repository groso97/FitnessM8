package com.example.FitnessM8.Repository;

import com.example.FitnessM8.Model.User;
import com.example.FitnessM8.Model.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout,Long> {
    List<Workout> findByUser(User user);
    Optional<Workout> findByWorkoutIdAndUser(Long workoutId, User user);

}
