package com.example.FitnessM8.Controller;

import com.example.FitnessM8.DTO.FitnessGoalDTO;
import com.example.FitnessM8.Model.Meal;
import com.example.FitnessM8.Model.User;
import com.example.FitnessM8.Model.Workout;
import com.example.FitnessM8.Service.FitnessGoalService;
import com.example.FitnessM8.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final UserService userService;
    private final FitnessGoalService fitnessGoalService;

    public DashboardController(UserService userService, FitnessGoalService fitnessGoalService) {
        this.userService = userService;
        this.fitnessGoalService = fitnessGoalService;
    }

    @GetMapping
    public String showDashboardPage(Model model, Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);

        List<Workout> workouts = user.getWorkouts();
        List<Meal> meals = user.getMeals();

        // Calculate daily calories
        int caloriesIn = meals.stream().mapToInt(Meal::getCalories).sum();
        int caloriesOut = workouts.stream().mapToInt(Workout::getCaloriesBurned).sum();
        int calorieBalance = caloriesIn - caloriesOut;

        // Prepare workout progress data
        List<String> progressDates = workouts.stream()
                .map(w -> w.getWorkoutDate().toString())
                .collect(Collectors.toList());

        List<Integer> progressCalories = workouts.stream()
                .map(Workout::getCaloriesBurned)
                .collect(Collectors.toList());

        // Get fitness goals for the user
        List<FitnessGoalDTO> fitnessGoals = fitnessGoalService.getFitnessGoalsForCurrentUser(email);

        // Featured workout (highest calories burned)
        Workout featuredWorkout = workouts.stream()
                .max(Comparator.comparingInt(Workout::getCaloriesBurned))
                .orElse(null);

        // Pass data to the model
        model.addAttribute("user", user);
        model.addAttribute("dailyCaloriesIn", caloriesIn);
        model.addAttribute("dailyCaloriesOut", caloriesOut);
        model.addAttribute("calorieBalance", calorieBalance);
        model.addAttribute("progressDates", progressDates);
        model.addAttribute("progressCalories", progressCalories);
        model.addAttribute("featuredWorkout", featuredWorkout);
        model.addAttribute("todaysMeals", meals); // Filter by today's date if needed
        model.addAttribute("fitnessGoals", fitnessGoals);

        return "dashboard";
    }
}
