package iths.com.food.Model;

import java.util.ArrayList;

/**
 * Created by Hristijan on 2016-11-16.
 */

public class Category {
    private ArrayList<Meal> meals;
    private String name;
    private double averageScore = 0;
    private int iconId;

    public Category(String name, ArrayList<Meal> meals, int iconId) {
        this.name = name;
        this.meals = meals;
        this.iconId = iconId;
        calculateAverageScore();
    }

    public String getName() {
        return name;
    }

    public double getAverageScore() {
        calculateAverageScore();
        return averageScore;
    }

    public ArrayList<Meal> getMeals() {
        return meals;
    }

    private void calculateAverageScore() {
        double sum = 0;
        if (this.meals != null) {
            for (Meal meal : meals) {
                sum += meal.getTotalScore();
            }
            averageScore = sum / meals.size();
        }
    }

    public int getIconId() {
        return iconId;
    }
}
