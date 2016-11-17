package iths.com.food.Helper;

import android.provider.BaseColumns;

/**
 * Created by Hristijan on 2016-11-15.
 *
 * Defines the database and its tables Meal and Category, and the SQL statement for creating those.
 */

final class DatabaseContract {

    // Database name and version
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "food.db";

    // SQL statements for creating table Meal and Category
    static final String SQL_CREATE_MEAL_ENTRY =
            "CREATE TABLE " + MealEntry.TABLE + " (" +
                    MealEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MealEntry.COLUMN_NAME + " TEXT," +
                    MealEntry.COLUMN_CATEGORY + " TEXT," +
                    MealEntry.COLUMN_DATE_TIME + " TEXT," +
                    MealEntry.COLUMN_DESCRIPTION + " TEXT," +
                    MealEntry.COLUMN_HEALTHY_SCORE + " INTEGER," +
                    MealEntry.COLUMN_TASTE_SCORE + " INTEGER," +
                    MealEntry.COLUMN_LONGITUDE + " DOUBLE," +
                    MealEntry.COLUMN_IMAGE_PATH + " TEXT," +
                    MealEntry.COLUMN_LATITUDE + " DOUBLE)";
    static final String SQL_CREATE_CATEGORY_ENTRY =
            "CREATE TABLE " + CategoryEntry.TABLE + " (" +
                CategoryEntry.COLUMN_NAME + " TEXT)";

    // Prevents instantiating of this class
    private DatabaseContract() {}

    // Table Meal
    static class MealEntry implements BaseColumns{
        static final String TABLE = "meal";
        static final String COLUMN_ID = "_id"; //primary key
        static final String COLUMN_NAME = "name";
        static final String COLUMN_DESCRIPTION = "description";
        static final String COLUMN_HEALTHY_SCORE = "healthy_score";
        static final String COLUMN_TASTE_SCORE = "taste_score";
        static final String COLUMN_CATEGORY = "category";
        static final String COLUMN_DATE_TIME = "date_time";
        static final String COLUMN_LONGITUDE = "longitude";
        static final String COLUMN_LATITUDE = "latitude";
        static final String COLUMN_IMAGE_PATH = "image_path";
    }

    //Table Category
    static class CategoryEntry implements BaseColumns{
        static final String TABLE = "category";
        static final String COLUMN_NAME = "name";
    }
}
