package iths.com.food.helper.db;

import java.util.ArrayList;

import iths.com.food.model.*;


/**
 * Created by Hristijan adn Willheim on 2016-11-16.
 *
 */

interface IDatabaseHelper {

    long insertMeal(IMeal meal);

    long insertCategory(String name, int iconID);

    IMeal getMeal(long id);

    ICategory getCategory(String categoryName);

    int updateMeal(IMeal meal);

    void deleteCategory(String categoryName);

    int deleteMeal(long id);

    ArrayList<ICategory> getCategories();
}
