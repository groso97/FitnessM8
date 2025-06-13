package com.example.FitnessM8.Controller;

import com.example.FitnessM8.DTO.WorkoutDTO;
import com.example.FitnessM8.Model.User;
import com.example.FitnessM8.Model.Workout;
import com.example.FitnessM8.Repository.UserRepository;
import com.example.FitnessM8.Service.WorkoutService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/workouts")
public class WorkoutController {

    public final WorkoutService workoutService;

    public final UserRepository userRepository;


    public WorkoutController(WorkoutService workoutService, UserRepository userRepository) {
        this.workoutService = workoutService;
        this.userRepository = userRepository;
    }


    @GetMapping
    public String showWorkoutPage(Model model, Principal principal){
        List<WorkoutDTO> workouts = workoutService.getWorkoutsForCurrentUser(principal.getName());

        model.addAttribute("workouts",workouts);

        return "workout";
    }

    @GetMapping("/create-workout")
    public String showCreateWorkoutPage(Model model){
        model.addAttribute("workoutDTO", new WorkoutDTO(null, "", "", null, null));
        model.addAttribute("isEdit", false);
        return "create-workout";
    }

    @GetMapping("/edit/{workoutId}")
    public String showEditWorkoutPage(@PathVariable Long workoutId, Principal principal, Model model) {
        String currentUserEmail = principal.getName();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Workout workout = workoutService.getWorkoutByIdAndUser(workoutId,user);

        WorkoutDTO workoutDTO = new WorkoutDTO(
                workout.getWorkoutId(),
                workout.getWorkoutName(),
                workout.getExerciseType(),
                workout.getDurationMinutes(),
                workout.getCaloriesBurned()
        );

        model.addAttribute("workoutDTO", workoutDTO);
        model.addAttribute("isEdit", true);

        return "create-workout";
    }


    //kreiranje workouta
    @PostMapping("/create-workout")
    public String createWorkout(@Valid @ModelAttribute("workoutDTO") WorkoutDTO workoutDTO,
                                BindingResult bindingResult,
                                Principal principal,
                                Model model,
                                RedirectAttributes redirectAttributes
                                ){

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false); // za reuse forme
            return "create-workout";
        }

        try {
            workoutService.createWorkout(workoutDTO, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Workout created successfully!");
            return "redirect:/workouts";

        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create workout.");
            model.addAttribute("isEdit", false);
            return "create-workout";
        }
    }

    //brisanje postojeceg workouta
    @PostMapping("/delete/{workoutId}")
    public String deleteWorkoutById(@PathVariable Long workoutId, Principal principal, RedirectAttributes redirectAttributes){
        String currentUserEmail  = principal.getName();

        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(()-> new RuntimeException("User not found."));

        try {
            workoutService.deleteWorkoutById(workoutId, user);
            redirectAttributes.addFlashAttribute("successMessage", "Workout deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete workout.");
        }

        return "redirect:/workouts";
    }

    //brisanje vise postojecih workoutsa
    @PostMapping("/delete-multiple")
    public String deleteMultipleGoals(@RequestParam(name = "selectedWorkouts", required = false) List<Long> selectedWorkoutIds,
                                      Principal principal,
                                      RedirectAttributes redirectAttributes) {
        if (selectedWorkoutIds == null || selectedWorkoutIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "No workouts selected for deletion.");
            return "redirect:/workouts";
        }

        String currentUserEmail = principal.getName();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            workoutService.deleteMultipleWorkouts(selectedWorkoutIds, user);
            redirectAttributes.addFlashAttribute("successMessage", "Selected workouts deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete selected workouts.");
        }

        return "redirect:/workouts";
    }



    //updateanje postojeceg workout
    @PostMapping("/update/{workoutId}")
    public String updateWorkoutById(@PathVariable Long workoutId,
                                  @Valid @ModelAttribute WorkoutDTO workoutDTO,
                                  BindingResult bindingResult,
                                  Principal principal,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "create-workout";
        }

        String currentUserEmail = principal.getName();

        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            workoutService.updateWorkoutById(workoutId, workoutDTO, user);
            redirectAttributes.addFlashAttribute("successMessage", "Workout updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update workout.");
        }

        return "redirect:/workouts";
    }
}
