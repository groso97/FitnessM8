package com.example.FitnessM8.Service;

import com.example.FitnessM8.DTO.MealDTO;
import com.example.FitnessM8.Mapper.MealMapper;
import com.example.FitnessM8.Model.Meal;
import com.example.FitnessM8.Model.User;
import com.example.FitnessM8.Repository.MealRepository;
import com.example.FitnessM8.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealService {

    public final MealRepository mealRepository;
    public final MealMapper mealMapper;
    public final UserRepository userRepository;

    public MealService(MealRepository mealRepository, MealMapper mealMapper, UserRepository userRepository) {
        this.mealRepository = mealRepository;
        this.mealMapper = mealMapper;
        this.userRepository = userRepository;
    }

    public List<MealDTO> getMealsForCurrentUser(String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new RuntimeException("User not found"));

        List<Meal> meals = mealRepository.findByUser(user);

        return meals.stream().map(mealMapper::toDTOo).toList();
    }

    public void createMeal(MealDTO mealDTO, String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new RuntimeException("User not found."));

        Meal meal = new Meal(
                mealDTO.mealName(),
                mealDTO.calories(),
                mealDTO.proteins(),
                mealDTO.fats(),
                mealDTO.carbs(),
                user
        );

        mealRepository.save(meal);
    }

    public Meal getMealByIdAndUser(Long mealId, User user) {
        return mealRepository.findByMealIdAndUser(mealId, user)
                .orElseThrow(() -> new RuntimeException("Meal not found or not yours"));
    }

    public void deleteMealById(Long mealId, User user){
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(()->new RuntimeException("Meal not found"));

        if(!meal.getUser().getUserId().equals(user.getUserId())){
            throw new RuntimeException("You are not authorized to delete this meal");
        }

        mealRepository.delete(meal);
    }

    public void deleteMultipleWorkouts(List<Long> mealIds, User user) {
        List<Meal> mealsToDelete = mealRepository.findAllById(mealIds);

        for (Meal meal : mealsToDelete) {
            if (!meal.getUser().getUserId().equals(user.getUserId())) {
                throw new RuntimeException("Unauthorized deletion attempt.");
            }
        }

        mealRepository.deleteAll(mealsToDelete);
    }

    public void updateMealById(Long mealId, MealDTO mealDTO, User user){
        Meal meal = mealRepository.findByMealIdAndUser(mealId, user)
                .orElseThrow(() -> new RuntimeException("Meal not found or not yours"));


        meal.setMealName(mealDTO.mealName());
        meal.setCalories(mealDTO.calories());
        meal.setProteins(mealDTO.proteins());
        meal.setFats(mealDTO.fats());
        meal.setCarbs(mealDTO.carbs());


        mealRepository.save(meal);
    }
}
