package com.example.FitnessM8.Controller;

import com.example.FitnessM8.DTO.MealDTO;
import com.example.FitnessM8.Model.Meal;
import com.example.FitnessM8.Model.User;
import com.example.FitnessM8.Repository.UserRepository;
import com.example.FitnessM8.Service.MealService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    public String showCreateMealPage(Model model){
        model.addAttribute("mealDTO", new MealDTO(null, "", null, null, null, null));
        model.addAttribute("isEdit", false);
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
    public String createMeal(@Valid @ModelAttribute("mealDTO") MealDTO mealDTO,
                             BindingResult bindingResult,
                             Principal principal,
                             Model model,
                             RedirectAttributes redirectAttributes
    ){

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false); // za reuse forme
            return "create-meal";
        }

        try {
            mealService.createMeal(mealDTO, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Meal created successfully!");
            return "redirect:/meals";
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create meal.");
            model.addAttribute("isEdit", false);
            return "create-meal";
        }
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

    //brisanje vise postojecih mealsa
    @PostMapping("/delete-multiple")
    public String deleteMultipleGoals(@RequestParam(name = "selectedMeals", required = false) List<Long> selectedMealIds,
                                      Principal principal,
                                      RedirectAttributes redirectAttributes) {
        if (selectedMealIds == null || selectedMealIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "No meals selected for deletion.");
            return "redirect:/meals";
        }

        String currentUserEmail = principal.getName();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            mealService.deleteMultipleWorkouts(selectedMealIds, user);
            redirectAttributes.addFlashAttribute("successMessage", "Selected meals deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete selected meals.");
        }

        return "redirect:/meals";
    }


    //updateanje postojeceg meala
    @PostMapping("/update/{mealId}")
    public String updateWorkoutById(@PathVariable Long mealId,
                                    @Valid @ModelAttribute MealDTO mealDTO,
                                    BindingResult bindingResult,
                                    Principal principal,
                                    Model model,
                                    RedirectAttributes redirectAttributes){

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "create-meal";
        }

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
