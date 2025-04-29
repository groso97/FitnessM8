package com.example.FitnessM8.Mapper;

import com.example.FitnessM8.DTO.FitnessGoalDTO;
import com.example.FitnessM8.Model.FitnessGoal;
import org.springframework.stereotype.Service;

@Service
public class FitnessGoalMapper {

    public FitnessGoalDTO toDTO(FitnessGoal fitnessGoal){
        return new FitnessGoalDTO(
                fitnessGoal.getFitnessGoalId(),
                fitnessGoal.getTitle(),
                fitnessGoal.getDescription(),
                fitnessGoal.getTargetDate(),
                fitnessGoal.isCompleted()
        );
    }

    public FitnessGoal toEntity(FitnessGoalDTO fitnessGoalDTO){
        FitnessGoal fitnessGoal = new FitnessGoal();

        fitnessGoal.setFitnessGoalId(fitnessGoalDTO.fitnessGoalId());
        fitnessGoal.setTitle(fitnessGoalDTO.title());
        fitnessGoal.setDescription(fitnessGoalDTO.description());
        fitnessGoal.setTargetDate(fitnessGoalDTO.targetDate());
        fitnessGoal.setCompleted(fitnessGoalDTO.completed());

        return fitnessGoal;
    }
}
