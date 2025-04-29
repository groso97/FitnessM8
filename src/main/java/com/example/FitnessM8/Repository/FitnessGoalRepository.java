package com.example.FitnessM8.Repository;

import com.example.FitnessM8.Model.FitnessGoal;
import com.example.FitnessM8.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FitnessGoalRepository extends JpaRepository<FitnessGoal,Long> {
    List<FitnessGoal> findByUser(User user);
    Optional<FitnessGoal> findByFitnessGoalIdAndUser(Long fitnessGoalId, User user);
}
