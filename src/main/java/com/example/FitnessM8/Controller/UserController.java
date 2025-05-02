package com.example.FitnessM8.Controller;

import com.example.FitnessM8.Model.User;
import com.example.FitnessM8.Service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/user-profile")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showUserProfile(Model model, Principal principal) {
        try {
            String userEmail = principal.getName();
            User user = userService.getUserByEmail(userEmail);
            model.addAttribute("user", user);
            return "user-profile";
        } catch (RuntimeException e) {
            return "redirect:/login"; // Ako korisnik ne postoji (npr. izbrisan), idi na login
        }
    }


    @PostMapping("/update")
    public String updateUserProfile(@ModelAttribute("user") User updatedUser, Principal principal, RedirectAttributes redirectAttributes) {
        boolean isChanged = userService.updateUserProfile(updatedUser, principal.getName());

        if (isChanged) {
            redirectAttributes.addFlashAttribute("successMessage", "Profile successfully updated!");
        } else {
            redirectAttributes.addFlashAttribute("infoMessage", "No changes were made.");
        }

        return "redirect:/user-profile";
    }

    @PostMapping("/delete")
    public String deleteUserAccount(Principal principal, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String userEmail = principal.getName();

        boolean isDeleted = userService.deleteUserAccount(userEmail);

        if (isDeleted) {
            try {
                request.logout(); // Odjavi korisnika nakon brisanja
            } catch (ServletException e) {
                e.printStackTrace(); // Možeš logirati grešku ako želiš
            }

            redirectAttributes.addFlashAttribute("successMessage", "Your account has been successfully deleted.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while deleting your account.");
            return "redirect:/user-profile";
        }
    }
}
