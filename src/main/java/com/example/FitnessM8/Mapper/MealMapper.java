package com.example.FitnessM8.Mapper;

import com.example.FitnessM8.DTO.MealDTO;
import com.example.FitnessM8.Model.Meal;
import org.springframework.stereotype.Service;

@Service
public class MealMapper {

    public MealDTO toDTOo(Meal meal){
        return new MealDTO(
                meal.getMealId(),
                meal.getMealName(),
                meal.getCalories(),
                meal.getProteins(),
                meal.getFats(),
                meal.getCarbs()
        );
    }

    public Meal toEntity(MealDTO mealDTO){
        Meal meal = new Meal();
        meal.setMealId(mealDTO.mealId());
        meal.setMealName(mealDTO.mealName());
        meal.setCalories(mealDTO.calories());
        meal.setProteins(mealDTO.proteins());
        meal.setFats(mealDTO.fats());
        meal.setCarbs(mealDTO.carbs());

        return meal;
    }
}
