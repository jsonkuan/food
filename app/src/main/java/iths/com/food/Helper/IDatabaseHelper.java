package iths.com.food.helper;

import java.util.ArrayList;

import iths.com.food.model.Category;
import iths.com.food.model.Meal;

/**
 * Created by Hristijan on 2016-11-16.
 *
 */

interface IDatabaseHelper {

    long insertMeal(Meal meal);

    long insertCategory(String name, int iconID);

    Meal getMeal(long id);

    int deleteMeal(long id);

    ArrayList<Category> getCategories();
}
