package com.example.FitnessM8.Controller;

import com.example.FitnessM8.Model.User;
import com.example.FitnessM8.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        String userEmail = principal.getName();

        User user = userService.getUserByEmail(userEmail);

        model.addAttribute("user", user);
        return "user-profile";
    }
}
