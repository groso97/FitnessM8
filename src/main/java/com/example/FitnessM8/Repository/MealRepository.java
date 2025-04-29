package com.example.FitnessM8.Repository;

import com.example.FitnessM8.Model.Meal;
import com.example.FitnessM8.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal,Long> {
    List<Meal> findByUser(User user);
    Optional<Meal> findByMealIdAndUser(Long mealId, User user);
}
