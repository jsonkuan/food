package iths.com.food.Model;

import java.util.ArrayList;

/**
 * Created by Hristijan on 2016-11-16.
 */

public class Category {
    private String name;
    private double averageScore;
    private ArrayList<Meal> meals;

    public void setMeals(ArrayList<Meal> meals) {
        this.meals = meals;
    }

    public Category(String name, ArrayList<Meal> meals) {
        this.name = name;
        this.meals = meals;

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

        for (Meal meal: meals) {
            sum += meal.getTotalScore();
        }

        averageScore = sum / meals.size();
    }

    public Meal getMeal(int id) {
        return meals.get(id);
    }
}
