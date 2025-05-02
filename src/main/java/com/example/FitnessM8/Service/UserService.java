package com.example.FitnessM8.Service;

import com.example.FitnessM8.Model.User;
import com.example.FitnessM8.Repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }



    public boolean updateUserProfile(User updatedUser, String currentEmail) {
        User existingUser = getUserByEmail(currentEmail);
        boolean isChanged = false;

        if (!existingUser.getFirstName().equals(updatedUser.getFirstName())) {
            existingUser.setFirstName(updatedUser.getFirstName());
            isChanged = true;
        }

        if (!existingUser.getLastName().equals(updatedUser.getLastName())) {
            existingUser.setLastName(updatedUser.getLastName());
            isChanged = true;
        }

        if (!existingUser.getUsername().equals(updatedUser.getUsername())) {
            existingUser.setUsername(updatedUser.getUsername());
            isChanged = true;
        }

        if (updatedUser.getAvatarUrl() != null && !updatedUser.getAvatarUrl().equals(existingUser.getAvatarUrl())) {
            existingUser.setAvatarUrl(updatedUser.getAvatarUrl());
            isChanged = true;
        }

        if (isChanged) {
            userRepository.save(existingUser);
        }

        return isChanged;
    }

    public boolean deleteUserAccount(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        try {
            userRepository.delete(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
