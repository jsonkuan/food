package iths.com.food.model;

import java.util.ArrayList;

/**
 * Created by Hristijan on 2016-11-16.
 */

public interface ICategory {

    String getName();

    double getAverageScore();

    ArrayList<IMeal> getMeals();

    int getIconId();
}
