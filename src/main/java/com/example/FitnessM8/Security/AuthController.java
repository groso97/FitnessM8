package com.example.FitnessM8.Security;

import com.example.FitnessM8.DTO.UserRegistrationDTO;
import com.example.FitnessM8.Model.User;
import com.example.FitnessM8.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class AuthController {

    private final UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model,HttpServletRequest request) {
        // Provjera da li je korisnik već prijavljen
        if (request.getUserPrincipal() != null) {
            return "redirect:/dashboard";  // Ako je korisnik prijavljen, preusmjerenje na dashboard
        }

        if (error != null) {
            model.addAttribute("loginErrorMessage", "Invalid email or password. Please try again.");
        }

        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new UserRegistrationDTO(null,null,null,null,null,null));
        return "registration";
    }



    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid UserRegistrationDTO userRegistrationDTO,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        if (!userRegistrationDTO.password().equals(userRegistrationDTO.confirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.user", "Passwords do not match.");
        }

        if (userRepository.findByEmail(userRegistrationDTO.email()).isPresent()) {
            bindingResult.rejectValue("email", "error.user", "Email already in use.");
        }

        if (userRepository.findByUsername(userRegistrationDTO.username()).isPresent()) {
            bindingResult.rejectValue("username", "error.user", "Username already in use.");
        }

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        User newUser = new User();
        newUser.setFirstName(userRegistrationDTO.firstname());
        newUser.setLastName(userRegistrationDTO.lastname());
        newUser.setUsername(userRegistrationDTO.username());
        newUser.setEmail(userRegistrationDTO.email());
        newUser.setPassword(passwordEncoder.encode(userRegistrationDTO.password()));
        newUser.setRegistrationDate(LocalDate.now());

        userRepository.save(newUser);

        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login.");

        return "redirect:/login";
    }
}
