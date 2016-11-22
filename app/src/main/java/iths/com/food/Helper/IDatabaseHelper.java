package iths.com.food.Helper;

import java.util.ArrayList;

import iths.com.food.Model.Category;
import iths.com.food.Model.Meal;

/**
 * Created by Hristijan on 2016-11-16.
 */

public interface IDatabaseHelper {

    public long insertMeal(Meal meal);

    public long insertCategory(String name);

    public Meal getMeal(long id);

    public int deleteMeal(long id);

    public ArrayList<Category> getCategories();
}
