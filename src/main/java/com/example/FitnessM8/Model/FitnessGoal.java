package com.example.FitnessM8.Model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "fitness_goals")
public class FitnessGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fitnessGoalId;

    @Column(nullable = false,length = 50)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column
    private LocalDate targetDate;

    @Column
    private boolean completed = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public FitnessGoal(){}

    public FitnessGoal(String title, String description, LocalDate targetDate, boolean completed, User user) {
        this.title = title;
        this.description = description;
        this.targetDate = targetDate;
        this.completed = completed;
        this.user = user;
    }

    public double getProgressPercent() {
        if (completed) {
            return 100.0;
        }
        // Ako nije završen, možete dodati logiku temeljem preostalog vremena ili cilja
        // Ovdje pretpostavljamo da imate neku logiku za napredak u postocima, npr. na temelju vremena
        long daysRemaining = targetDate.toEpochDay() - LocalDate.now().toEpochDay();
        if (daysRemaining > 0) {
            return 100.0 - (daysRemaining * 100.0 / 30);  // Pretpostavka da je cilj postavljen na 30 dana
        }
        return 0.0;
    }


    public Long getFitnessGoalId() {
        return fitnessGoalId;
    }

    public void setFitnessGoalId(Long fitnessGoalId) {
        this.fitnessGoalId = fitnessGoalId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
