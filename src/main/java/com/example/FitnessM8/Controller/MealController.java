package com.example.FitnessM8.Controller;

import com.example.FitnessM8.DTO.MealDTO;
import com.example.FitnessM8.DTO.WorkoutDTO;
import com.example.FitnessM8.Model.Meal;
import com.example.FitnessM8.Model.User;
import com.example.FitnessM8.Model.Workout;
import com.example.FitnessM8.Repository.UserRepository;
import com.example.FitnessM8.Service.MealService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/meals")
public class MealController {

    public final MealService mealService;

    public final UserRepository userRepository;


    public MealController(MealService mealService, UserRepository userRepository) {
        this.mealService = mealService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showMealPage(Model model, Principal principal){
        List<MealDTO> meals = mealService.getMealsForCurrentUser(principal.getName());

        model.addAttribute("meals", meals);

        return "meal";
    }

    @GetMapping("/create-meal")
    public String showCreateMealPage(){
        return "create-meal";
    }

    @GetMapping("/edit/{mealId}")
    public String showEditMealPage(@PathVariable Long mealId, Principal principal, Model model) {
        String currentUserEmail = principal.getName();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Meal meal = mealService.getMealByIdAndUser(mealId,user);

        MealDTO mealDTO = new MealDTO(
                meal.getMealId(),
                meal.getMealName(),
                meal.getCalories(),
                meal.getProteins(),
                meal.getFats(),
                meal.getCarbs()
        );

        model.addAttribute("mealDTO", mealDTO);
        model.addAttribute("isEdit", true);

        return "create-meal";
    }

    //kreiranje novog meala
    @PostMapping("/create-meal")
    public String createMeal(@ModelAttribute MealDTO mealDTO, Principal principal, RedirectAttributes redirectAttributes){
        try {
            mealService.createMeal(mealDTO, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Meal created successfully!");
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create meal.");
        }
        return "redirect:/meals";
    }


    //brisanje postojeceg meala
    @PostMapping("/delete/{mealId}")
    public String deleteWorkoutById(@PathVariable Long mealId, Principal principal, RedirectAttributes redirectAttributes){
        String currentUserEmail  = principal.getName();

        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(()-> new RuntimeException("User not found."));

        try {
            mealService.deleteMealById(mealId, user);
            redirectAttributes.addFlashAttribute("successMessage", "Meal deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete meal.");
        }

        return "redirect:/meals";
    }


    //updateanje postojeceg meala
    @PostMapping("/update/{mealId}")
    public String updateWorkoutById(@PathVariable Long mealId,
                                    @ModelAttribute MealDTO mealDTO,
                                    Principal principal,
                                    RedirectAttributes redirectAttributes){

        String currentUserEmail = principal.getName();

        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            mealService.updateMealById(mealId, mealDTO, user);
            redirectAttributes.addFlashAttribute("successMessage", "Meal updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update meal.");
        }

        return "redirect:/meals";
    }
}
