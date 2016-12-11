package iths.com.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Hristijan on 2016-11-16.
 *
 */

public class Category {
    private String name;
    private ArrayList<Meal> meals;
    private int iconId;

    public Category(String name, ArrayList<Meal> meals, int iconId) {
        this.name = name;
        this.meals = meals;
        this.iconId = iconId;

        sortMeals();
    }

    public String getName() {
        return name;
    }

    public double getAverageScore() {
        double sum = 0;
        for (Meal meal: meals) {
            sum += meal.getTotalScore();
        }

        return sum / meals.size();
    }

    public ArrayList<Meal> getMeals() {
        return meals;
    }

    public int getIconId() {
        return iconId;
    }

    private void sortMeals() {
        Collections.sort(meals, new Comparator<Meal>() {
            @Override
            public int compare(Meal m1, Meal m2) {
                return m2.getDateTime().compareTo(m1.getDateTime());
            }
        });
    }
}
