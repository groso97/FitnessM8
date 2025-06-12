package com.example.FitnessM8.Security;

import com.example.FitnessM8.Model.User;
import com.example.FitnessM8.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String showRegistrationPage() {
        return "registration";
    }



    @PostMapping("/register")
    public String registerUser(@RequestParam String firstname,
                               @RequestParam String lastname,
                               @RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam("confirm-password") String confirmPassword,
                               Model model, RedirectAttributes redirectAttributes) {

        if (!password.equals(confirmPassword)) {
            model.addAttribute("passwordError", "Passwords do not match!");
            return "registration";
        }

        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("emailError", "Email already in use!");
            return "registration";
        }

        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("usernameError", "Username already in use!");
            return "registration";
        }

        User newUser = new User();
        newUser.setFirstName(firstname);
        newUser.setLastName(lastname);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRegistrationDate(LocalDate.now());

        userRepository.save(newUser);

        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login.");

        return "redirect:/login";
    }
}
