package com.example.FitnessM8.Security;

import com.example.FitnessM8.Model.User;
import com.example.FitnessM8.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final CustomUserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String showLoginPage(HttpServletRequest request) {
        // Provjera da li je korisnik već prijavljen
        if (request.getUserPrincipal() != null) {
            return "redirect:/dashboard";  // Ako je korisnik prijavljen, preusmjerenje na dashboard
        }
        return "login";  // Inače prikazuj login stranicu
    }

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "registration";  // Pokaži stranicu za registraciju
    }



    @PostMapping("/register")
    public String registerUser(@RequestParam String firstname, @RequestParam String lastname, @RequestParam String username, @RequestParam String email, @RequestParam String password,
                               Model model, RedirectAttributes redirectAttributes) {

        // Provjera postoji li korisnik s tim emailom
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email already in use!");
            return "registration";  // Vraća na stranicu ako postoji problem
        }

        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username already in use!");
            return "registration";  // Vraća na stranicu ako postoji problem
        }

        // Kreiranje novog korisnika
        User newUser = new User();
        newUser.setFirstName(firstname);
        newUser.setLastName(lastname);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));  // Šifriranje lozinke
        newUser.setRegistrationDate(LocalDate.now());

        // Spremanje korisnika u bazu podataka
        userRepository.save(newUser);

        // Dodavanje flash poruke za uspjeh
        redirectAttributes.addFlashAttribute("message", "Registration successful! Please login.");

        // Preusmjeravanje na login stranicu
        return "redirect:/login";
    }

}
