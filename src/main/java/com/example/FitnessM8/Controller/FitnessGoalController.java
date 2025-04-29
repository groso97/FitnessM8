package com.example.FitnessM8.Controller;

import com.example.FitnessM8.DTO.FitnessGoalDTO;
import com.example.FitnessM8.Model.FitnessGoal;
import com.example.FitnessM8.Model.User;
import com.example.FitnessM8.Repository.UserRepository;
import com.example.FitnessM8.Service.FitnessGoalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String showCreateFitnessGoalPage(){
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
    public String createFitnessGoal(@ModelAttribute FitnessGoalDTO fitnessGoalDTO, Principal principal, RedirectAttributes redirectAttributes){
        try {
            fitnessGoalService.createFitnessGoal(fitnessGoalDTO, principal.getName());
            redirectAttributes.addFlashAttribute("success", "Fitness goal created successfully!");
        }catch (RuntimeException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/fitness-goals";
    }


    //brisanje postojeceg fitness goala
    @PostMapping("/delete/{fitnessGoalId}")
    public String deleteWorkoutById(@PathVariable Long fitnessGoalId, Principal principal, RedirectAttributes redirectAttributes){
        String currentUserEmail  = principal.getName();

        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(()-> new RuntimeException("User not found."));

        try {
            fitnessGoalService.deleteFitnessGoalById(fitnessGoalId, user);
            redirectAttributes.addFlashAttribute("success", "Fitness goal deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete fitness goal.");
        }

        return "redirect:/fitness-goals";
    }

    //brisanje vise postojecih fitness goalsa
    @PostMapping("/delete-multiple")
    public String deleteMultipleGoals(@RequestParam(name = "selectedGoals", required = false) List<Long> selectedGoalIds,
                                      Principal principal,
                                      RedirectAttributes redirectAttributes) {
        if (selectedGoalIds == null || selectedGoalIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "No goals selected for deletion.");
            return "redirect:/fitness-goals";
        }

        String currentUserEmail = principal.getName();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            fitnessGoalService.deleteMultipleGoals(selectedGoalIds, user);
            redirectAttributes.addFlashAttribute("success", "Selected goals deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete selected goals.");
        }

        return "redirect:/fitness-goals";
    }



    //updateanje postojeceg fitness goala
    @PostMapping("/update/{fitnessGoalId}")
    public String updateFitnessGoalById(@PathVariable Long fitnessGoalId,
                                    @ModelAttribute FitnessGoalDTO fitnessGoalDTO,
                                    Principal principal,
                                    RedirectAttributes redirectAttributes){

        String currentUserEmail = principal.getName();

        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        try {
            fitnessGoalService.updateFitnessGoalById(fitnessGoalId, fitnessGoalDTO, user);
            redirectAttributes.addFlashAttribute("success", "Fitness goal updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update fitness goal.");
        }

        return "redirect:/fitness-goals";
    }
}
