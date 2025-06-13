package com.example.FitnessM8.Controller;

import com.example.FitnessM8.DTO.FitnessGoalDTO;
import com.example.FitnessM8.DTO.MealDTO;
import com.example.FitnessM8.Model.FitnessGoal;
import com.example.FitnessM8.Model.User;
import com.example.FitnessM8.Repository.UserRepository;
import com.example.FitnessM8.Service.FitnessGoalService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/fitness-goals")
public class FitnessGoalController {

    private final FitnessGoalService fitnessGoalService;

    private final UserRepository userRepository;

    public FitnessGoalController(FitnessGoalService fitnessGoalService, UserRepository userRepository) {
        this.fitnessGoalService = fitnessGoalService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String showFitnessGoalsPage(Model model, Principal principal){
        List<FitnessGoalDTO> fitnessGoals = fitnessGoalService.getFitnessGoalsForCurrentUser(principal.getName());

        int progressPercentage = fitnessGoalService.calculateProgressPercentage(fitnessGoals);

        model.addAttribute("fitnessGoals", fitnessGoals);
        model.addAttribute("progressPercentage", progressPercentage);

        return "fitness-goal";
    }

    @GetMapping("/create-fitness-goal")
    public String showCreateFitnessGoalPage(Model model){
        model.addAttribute("fitnessGoalDTO", new FitnessGoalDTO(null, "", null, null, null));
        model.addAttribute("isEdit", false);
        return "create-fitness-goal";
    }



    @GetMapping("/edit/{fitnessGoalId}")
    public String showEditFitnessGoalPage(@PathVariable Long fitnessGoalId, Principal principal, Model model) {
        String currentUserEmail = principal.getName();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FitnessGoal fitnessGoal = fitnessGoalService.getFitnessGoalByIdAndUser(fitnessGoalId,user);

        FitnessGoalDTO fitnessGoalDTO = new FitnessGoalDTO(
                fitnessGoal.getFitnessGoalId(),
                fitnessGoal.getTitle(),
                fitnessGoal.getDescription(),
                fitnessGoal.getTargetDate(),
                fitnessGoal.isCompleted()
        );

        model.addAttribute("fitnessGoalDTO", fitnessGoalDTO);
        model.addAttribute("isEdit", true);

        return "create-fitness-goal";
    }

    //kreiranje novog fitness goala
    @PostMapping("/create-fitness-goal")
    public String createFitnessGoal(@Valid @ModelAttribute("fitnessGoalDTO") FitnessGoalDTO fitnessGoalDTO,
                                    BindingResult bindingResult,
                                    Principal principal,
                                    Model model,
                                    RedirectAttributes redirectAttributes
    ){

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false); // za reuse forme
            return "create-fitness-goal";
        }

        try {
            fitnessGoalService.createFitnessGoal(fitnessGoalDTO, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Fitness goal created successfully!");
            return "redirect:/fitness-goals";
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create fitness goal.");
            model.addAttribute("isEdit", false);
            return "create-fitness-goal";
        }
    }


    //brisanje postojeceg fitness goala
    @PostMapping("/delete/{fitnessGoalId}")
    public String deleteWorkoutById(@PathVariable Long fitnessGoalId, Principal principal, RedirectAttributes redirectAttributes){
        String currentUserEmail  = principal.getName();

        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(()-> new RuntimeException("User not found."));

        try {
            fitnessGoalService.deleteFitnessGoalById(fitnessGoalId, user);
            redirectAttributes.addFlashAttribute("successMessage", "Fitness goal deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete fitness goal.");
        }

        return "redirect:/fitness-goals";
    }

    //brisanje vise postojecih fitness goalsa
    @PostMapping("/delete-multiple")
    public String deleteMultipleGoals(@RequestParam(name = "selectedGoals", required = false) List<Long> selectedGoalIds,
                                      Principal principal,
                                      RedirectAttributes redirectAttributes) {
        if (selectedGoalIds == null || selectedGoalIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "No goals selected for deletion.");
            return "redirect:/fitness-goals";
        }

        String currentUserEmail = principal.getName();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            fitnessGoalService.deleteMultipleGoals(selectedGoalIds, user);
            redirectAttributes.addFlashAttribute("successMessage", "Selected goals deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete selected goals.");
        }

        return "redirect:/fitness-goals";
    }



    //updateanje postojeceg fitness goala
    @PostMapping("/update/{fitnessGoalId}")
    public String updateFitnessGoalById(@PathVariable Long fitnessGoalId,
                                    @Valid @ModelAttribute FitnessGoalDTO fitnessGoalDTO,
                                    BindingResult bindingResult,
                                    Principal principal,
                                    Model model,
                                    RedirectAttributes redirectAttributes
    ){

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "create-fitness-goal";
        }

        String currentUserEmail = principal.getName();

        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            fitnessGoalService.updateFitnessGoalById(fitnessGoalId, fitnessGoalDTO, user);
            redirectAttributes.addFlashAttribute("successMessage", "Fitness goal updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update fitness goal.");
        }

        return "redirect:/fitness-goals";
    }
}
