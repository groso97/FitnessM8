package com.example.FitnessM8.Controller;

import com.example.FitnessM8.DTO.WorkoutDTO;
import com.example.FitnessM8.Model.User;
import com.example.FitnessM8.Model.Workout;
import com.example.FitnessM8.Repository.UserRepository;
import com.example.FitnessM8.Service.WorkoutService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String showCreateWorkoutPage(){
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
    public String createWorkout(@ModelAttribute WorkoutDTO workoutDTO, Principal principal
    , RedirectAttributes redirectAttributes){

        try {
            workoutService.createWorkout(workoutDTO, principal.getName());
            redirectAttributes.addFlashAttribute("success", "Workout created successfully!");
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/workouts";
    }

    //brisanje postojeceg workouta
    @PostMapping("/delete/{workoutId}")
    public String deleteWorkoutById(@PathVariable Long workoutId, Principal principal, RedirectAttributes redirectAttributes){
        String currentUserEmail  = principal.getName();

        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(()-> new RuntimeException("User not found."));

        try {
            workoutService.deleteWorkoutById(workoutId, user);
            redirectAttributes.addFlashAttribute("success", "Workout deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete workout.");
        }

        return "redirect:/workouts";
    }


    //updateanje postojeceg workout
    @PostMapping("/update/{workoutId}")
    public String updateWorkoutById(@PathVariable Long workoutId,
                                  @ModelAttribute WorkoutDTO workoutDTO,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes){

        String currentUserEmail = principal.getName();

        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            workoutService.updateWorkoutById(workoutId, workoutDTO, user);
            redirectAttributes.addFlashAttribute("success", "Workout updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update workout.");
        }

        return "redirect:/workouts";
    }
}
