package com.example.FitnessM8.Model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mealId;

    @Column(nullable = false)
    private LocalDate mealDate;

    @Column(length = 50,nullable = false)
    private String mealName;

    @Column
    private int calories;

    @Column
    private int proteins;

    @Column
    private int fats;

    @Column
    private int carbs;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    public Meal(){}

    public Meal(String mealName, int calories, int proteins, int fats, int carbs, User user) {
        this.mealDate = LocalDate.now();
        this.mealName = mealName;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbs = carbs;
        this.user = user;
    }

    public Long getMealId() {
        return mealId;
    }

    public void setMealId(Long mealId) {
        this.mealId = mealId;
    }

    public LocalDate getMealDate() {
        return mealDate;
    }

    public void setMealDate(LocalDate mealDate) {
        this.mealDate = mealDate;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getProteins() {
        return proteins;
    }

    public void setProteins(int proteins) {
        this.proteins = proteins;
    }

    public int getFats() {
        return fats;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
