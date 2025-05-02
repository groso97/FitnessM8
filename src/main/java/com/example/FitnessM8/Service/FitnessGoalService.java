package com.example.FitnessM8.Service;

import com.example.FitnessM8.DTO.FitnessGoalDTO;
import com.example.FitnessM8.Mapper.FitnessGoalMapper;
import com.example.FitnessM8.Model.FitnessGoal;
import com.example.FitnessM8.Model.User;
import com.example.FitnessM8.Repository.FitnessGoalRepository;
import com.example.FitnessM8.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FitnessGoalService {

    private final FitnessGoalRepository fitnessGoalRepository;

    private final UserRepository userRepository;

    private final FitnessGoalMapper fitnessGoalMapper;

    public FitnessGoalService(FitnessGoalRepository fitnessGoalRepository, UserRepository userRepository, FitnessGoalMapper fitnessGoalMapper) {
        this.fitnessGoalRepository = fitnessGoalRepository;
        this.userRepository = userRepository;
        this.fitnessGoalMapper = fitnessGoalMapper;
    }


    public List<FitnessGoalDTO> getFitnessGoalsForCurrentUser(String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new RuntimeException("User not found"));

        List<FitnessGoal> fitnessGoals = fitnessGoalRepository.findByUser(user);

        return fitnessGoals.stream().map(fitnessGoalMapper::toDTO).toList();
    }

    public void createFitnessGoal(FitnessGoalDTO fitnessGoalDTO, String userEmail){
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new RuntimeException("User not found."));

        boolean isCompleted = Boolean.TRUE.equals(fitnessGoalDTO.completed());

        FitnessGoal fitnessGoal = new FitnessGoal(
                fitnessGoalDTO.title(),
                fitnessGoalDTO.description(),
                fitnessGoalDTO.targetDate(),
                isCompleted,
                user
        );

        fitnessGoalRepository.save(fitnessGoal);
    }

    public FitnessGoal getFitnessGoalByIdAndUser(Long fitnessGoalId, User user) {
        return fitnessGoalRepository.findByFitnessGoalIdAndUser(fitnessGoalId, user)
                .orElseThrow(() -> new RuntimeException("Fitness goal not found or not yours"));
    }

    public void deleteFitnessGoalById(Long fitnessGoalId, User user){
        FitnessGoal fitnessGoal = fitnessGoalRepository.findById(fitnessGoalId)
                .orElseThrow(()->new RuntimeException("Fitness goal not found"));

        if(!fitnessGoal.getUser().getUserId().equals(user.getUserId())){
            throw new RuntimeException("You are not authorized to delete this fitness goal");
        }

        fitnessGoalRepository.delete(fitnessGoal);
    }

    public void deleteMultipleGoals(List<Long> goalIds, User user) {
        List<FitnessGoal> goalsToDelete = fitnessGoalRepository.findAllById(goalIds);

        for (FitnessGoal goal : goalsToDelete) {
            if (!goal.getUser().getUserId().equals(user.getUserId())) {
                throw new RuntimeException("Unauthorized deletion attempt.");
            }
        }

        fitnessGoalRepository.deleteAll(goalsToDelete);
    }


    public void updateFitnessGoalById(Long fitnessGoalId, FitnessGoalDTO fitnessGoalDTO, User user) {
        // Pronalazi fitness goal koji odgovara korisniku
        FitnessGoal fitnessGoal = fitnessGoalRepository.findByFitnessGoalIdAndUser(fitnessGoalId, user)
                .orElseThrow(() -> new RuntimeException("Fitness goal not found or not yours"));

        // Ažuriranje podataka cilja
        fitnessGoal.setTitle(fitnessGoalDTO.title());
        fitnessGoal.setDescription(fitnessGoalDTO.description());
        fitnessGoal.setTargetDate(fitnessGoalDTO.targetDate());

        // Ažuriranje statusa completed (true ili false)
        fitnessGoal.setCompleted(Boolean.TRUE.equals(fitnessGoalDTO.completed())); // Ovo osigurava ispravno postavljanje

        // Spremanje ažuriranog fitness goal-a u bazu
        fitnessGoalRepository.save(fitnessGoal);
    }


    //racunanje postotka "completed" fitness goalsa
    public int calculateProgressPercentage(List<FitnessGoalDTO> goals) {
        if (goals.isEmpty()) {
            return 0;
        }

        long completedCount = goals.stream()
                .filter(FitnessGoalDTO::completed)
                .count();

        return (int) Math.round((double) completedCount / goals.size() * 100);
    }
}
