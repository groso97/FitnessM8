package com.example.FitnessM8.Security;

import com.example.FitnessM8.Model.User;
import com.example.FitnessM8.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Traži korisnika po emailu
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            System.out.println("User found: " + email);  // Dodaj logiranje

            // Vraća Spring Security User s korisničkim podacima i šifrom
            return org.springframework.security.core.userdetails.User.withUsername(user.get().getEmail())
                    .password(user.get().getPassword())
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }
}
