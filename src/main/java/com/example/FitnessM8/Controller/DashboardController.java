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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

        // Današnji podaci
        LocalDate today = LocalDate.now();
        int caloriesInToday = meals.stream()
                .filter(m -> m.getMealDate().equals(today))
                .mapToInt(Meal::getCalories).sum();

        int caloriesOutToday = workouts.stream()
                .filter(w -> w.getWorkoutDate().equals(today))
                .mapToInt(Workout::getCaloriesBurned).sum();

        int calorieBalance = caloriesInToday - caloriesOutToday;

        // Kalorije za zadnjih 7 dana
        LocalDate weekAgo = today.minusDays(6);
        Map<LocalDate, Integer> mealCaloriesPerDay = meals.stream()
                .filter(m -> !m.getMealDate().isBefore(weekAgo))
                .collect(Collectors.groupingBy(Meal::getMealDate,
                        Collectors.summingInt(Meal::getCalories)));

        Map<LocalDate, Integer> workoutCaloriesPerDay = workouts.stream()
                .filter(w -> !w.getWorkoutDate().isBefore(weekAgo))
                .collect(Collectors.groupingBy(Workout::getWorkoutDate,
                        Collectors.summingInt(Workout::getCaloriesBurned)));

        List<String> last7Days = new ArrayList<>();
        List<Integer> caloriesInList = new ArrayList<>();
        List<Integer> caloriesOutList = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = weekAgo.plusDays(i);
            last7Days.add(date.toString());
            caloriesInList.add(mealCaloriesPerDay.getOrDefault(date, 0));
            caloriesOutList.add(workoutCaloriesPerDay.getOrDefault(date, 0));
        }

        // Najbolji trening
        Workout featuredWorkout = workouts.stream()
                .max(Comparator.comparingInt(Workout::getCaloriesBurned))
                .orElse(null);

        // Ciljevi
        List<FitnessGoalDTO> fitnessGoals = fitnessGoalService.getFitnessGoalsForCurrentUser(email);

        // Model
        model.addAttribute("user", user);
        model.addAttribute("dailyCaloriesIn", caloriesInToday);
        model.addAttribute("dailyCaloriesOut", caloriesOutToday);
        model.addAttribute("calorieBalance", calorieBalance);
        model.addAttribute("featuredWorkout", featuredWorkout);
        model.addAttribute("todaysMeals", meals.stream().filter(m -> m.getMealDate().equals(today)).toList());
        model.addAttribute("fitnessGoals", fitnessGoals);
        model.addAttribute("last7Days", last7Days);
        model.addAttribute("caloriesInList", caloriesInList);
        model.addAttribute("caloriesOutList", caloriesOutList);

        return "dashboard";
    }
}
