package com.example.FitnessM8.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegistrationDTO(
        @NotBlank(message = "First name is required.")
        @Size(max = 50)
        @Pattern(regexp = "^[A-Za-zčćžšđČĆŽŠĐ\\s-]+$", message = "First name must not contain numbers or special characters.")
        String firstname,

        @NotBlank(message = "Last name is required.")
        @Size(max = 50)
        @Pattern(regexp = "^[A-Za-zčćžšđČĆŽŠĐ\\s-]+$", message = "Last name must not contain numbers or special characters.")
        String lastname,

        @NotBlank(message = "Username is required.")
        @Size(max = 50)
        String username,

        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email format.")
        @Size(max = 50)
        String email,

        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{6,}$",
                message = "The password must be at least 6 characters long and contain at least one uppercase letter, one number, and one special character."
        )
        @NotBlank(message = "Password is required.")
        String password,

        @NotBlank(message = "Confirm password is required.")
        String confirmPassword
) {
}
