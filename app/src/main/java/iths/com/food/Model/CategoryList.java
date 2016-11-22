package iths.com.food.Model;

import java.util.ArrayList;

/**
 * Created by Hristijan on 2016-11-17.
 *
 *  fel enligt: composition over inheritance
 *  Borde istället skapa en CategoryList som har en ArrayList som iv med en add() metod
 *  eftersom vi behöver troligtis inte ArrayLists metoder.
 *
 *  följande är en bra guildline:
 *      I wouldn't extend ArrayList<> unless I really had to, preferring composition over inheritance
 */

public class CategoryList extends ArrayList<Category> {

    public ArrayList<String> getCategoryNames(){
        ArrayList<String> categoryNames = new ArrayList<>();

        for (Category c : this) {
            categoryNames.add(c.getName());
        }

        return categoryNames;
    }

    public double getCategoryScore(String categoryName){
        double score = 0;

        for (Category c : this) {
            if(categoryName.equals(c.getName()))
                score =  c.getAverageScore();
        }

        return score;
    }

    public ArrayList<Meal> getMeals(String categoryName){
        ArrayList<Meal> meals = null;

        for (Category c : this) {
            if(categoryName.equals(c.getName()))
                 meals =  c.getMeals();
        }

        return meals;
    }

    public Category getCategory(String categoryName){
        Category category = null;

        for (Category c : this) {
            if(categoryName.equals(c.getName()))
                category =  c;
        }

        return category;
    }
}

