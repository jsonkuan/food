package iths.com.food.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import iths.com.food.Model.Category;
import iths.com.food.Model.CategoryList;
import iths.com.food.Model.Meal;

/**
 * Created by Hristijan on 2016-11-15.
 */

public class DatabaseHelper extends SQLiteOpenHelper implements IDatabaseHelper{

    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // run the SQL script for creating necessary entries
        // run the SQL statements one by one, due to limitation in execSQL()
        for (String sql: DatabaseContract.SQL_CREATE_SCRIPT.split(";")) {
            db.execSQL(sql);
        }
    }

    // Do nothing here yet
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public long insertMeal(Meal meal){

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.MealEntry.COLUMN_NAME, meal.getName());
        values.put(DatabaseContract.MealEntry.COLUMN_CATEGORY, meal.getCategory());
        values.put(DatabaseContract.MealEntry.COLUMN_DATE_TIME, meal.getDateTime());
        values.put(DatabaseContract.MealEntry.COLUMN_DESCRIPTION, meal.getDescription());
        values.put(DatabaseContract.MealEntry.COLUMN_HEALTHY_SCORE, meal.getHealthyScore());
        values.put(DatabaseContract.MealEntry.COLUMN_TASTE_SCORE, meal.getTasteScore());
        values.put(DatabaseContract.MealEntry.COLUMN_LONGITUDE, meal.getLongitude());
        values.put(DatabaseContract.MealEntry.COLUMN_LATITUDE, meal.getLatitude());
        values.put(DatabaseContract.MealEntry.COLUMN_IMAGE_PATH, meal.getImagePath());


        return getWritableDatabase().insert(DatabaseContract.MealEntry.TABLE, null, values);
    }

    public long insertCategory(String name, int iconID){

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CategoryEntry.COLUMN_NAME, name);
        values.put(DatabaseContract.CategoryEntry.COLUMN_ICON_ID, iconID);

        return getWritableDatabase().insert(DatabaseContract.CategoryEntry.TABLE, null, values);
    }

    public Meal getMeal(long id){

        String[] projection = {
                DatabaseContract.MealEntry.COLUMN_NAME,
                DatabaseContract.MealEntry.COLUMN_CATEGORY,
                DatabaseContract.MealEntry.COLUMN_DATE_TIME,
                DatabaseContract.MealEntry.COLUMN_DESCRIPTION,
                DatabaseContract.MealEntry.COLUMN_HEALTHY_SCORE,
                DatabaseContract.MealEntry.COLUMN_TASTE_SCORE,
                DatabaseContract.MealEntry.COLUMN_LONGITUDE,
                DatabaseContract.MealEntry.COLUMN_LATITUDE,
                DatabaseContract.MealEntry.COLUMN_IMAGE_PATH
        };


        String selection = DatabaseContract.MealEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        String sortOrder = DatabaseContract.MealEntry.COLUMN_TASTE_SCORE + " DESC";

        Meal meal = new Meal();
        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(
                    DatabaseContract.MealEntry.TABLE,         // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            meal.setId(id);
            while (cursor.moveToNext()) {
                meal.setName( cursor.getString( cursor.getColumnIndexOrThrow(DatabaseContract.MealEntry.COLUMN_NAME)));
                meal.setCategory( cursor.getString( cursor.getColumnIndexOrThrow(DatabaseContract.MealEntry.COLUMN_CATEGORY)));
                meal.setDescription( cursor.getString( cursor.getColumnIndexOrThrow(DatabaseContract.MealEntry.COLUMN_DESCRIPTION)));
                meal.setHealthyScore( cursor.getInt( cursor.getColumnIndexOrThrow(DatabaseContract.MealEntry.COLUMN_HEALTHY_SCORE)));
                meal.setTasteScore( cursor.getInt( cursor.getColumnIndexOrThrow(DatabaseContract.MealEntry.COLUMN_TASTE_SCORE)));
                meal.setDateTime( cursor.getString( cursor.getColumnIndexOrThrow(DatabaseContract.MealEntry.COLUMN_DATE_TIME)));
                meal.setImagePath( cursor.getString( cursor.getColumnIndexOrThrow(DatabaseContract.MealEntry.COLUMN_IMAGE_PATH)));
                meal.setLongitude( cursor.getDouble( cursor.getColumnIndexOrThrow(DatabaseContract.MealEntry.COLUMN_LONGITUDE)));
                meal.setLatitude( cursor.getDouble( cursor.getColumnIndexOrThrow(DatabaseContract.MealEntry.COLUMN_LATITUDE)));
            }
        }
        catch (Exception e) {
            Log.d(TAG, e.getMessage());
            meal =  null;
        }
        finally {
            if(cursor != null)
                cursor.close();
        }

        return meal;
    }

    public ArrayList<Category> getCategories(){

        ArrayList<Category> categories = new ArrayList<>();

        String[] projection = {
                DatabaseContract.CategoryEntry.COLUMN_NAME
        };


        Cursor cursor = null;

        try {
            cursor = getReadableDatabase().query(
                    DatabaseContract.CategoryEntry.TABLE,         // The table to query
                    projection,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            while (cursor.moveToNext()) {

                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CategoryEntry.COLUMN_NAME));
                Category category = getCategory(categoryName);
                categories.add(category);
            }
        }
        catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        finally {
            if(cursor != null)
                cursor.close();
        }

        return  categories;
    }

    public int updateMeal(Meal meal){

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.MealEntry.COLUMN_NAME, meal.getName());
        values.put(DatabaseContract.MealEntry.COLUMN_CATEGORY, meal.getCategory());
        values.put(DatabaseContract.MealEntry.COLUMN_DESCRIPTION, meal.getDescription());
        values.put(DatabaseContract.MealEntry.COLUMN_HEALTHY_SCORE, meal.getHealthyScore());
        values.put(DatabaseContract.MealEntry.COLUMN_TASTE_SCORE, meal.getTasteScore());
        values.put(DatabaseContract.MealEntry.COLUMN_IMAGE_PATH, meal.getImagePath());

        String selection = DatabaseContract.MealEntry.COLUMN_ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(meal.getId()) };

        int count = getWritableDatabase().update(
                DatabaseContract.MealEntry.TABLE,
                values,
                selection,
                selectionArgs);
        return count;
    }

    public int deleteMeal(long id){

        String selection = DatabaseContract.MealEntry.COLUMN_ID + " LIKE ?";

        String[] selectionArgs = { String.valueOf(id) };

        return getWritableDatabase().delete(DatabaseContract.MealEntry.TABLE, selection, selectionArgs);
    }

    public void deleteCategory(String categoryName){
        // SQL-query for deleting all meals belonging to this category
        String deleteMealsSQL = "DELETE FROM " + DatabaseContract.MealEntry.TABLE +
                                " WHERE " + DatabaseContract.MealEntry.COLUMN_CATEGORY + "='" + categoryName + "'";
        // execute query
        getWritableDatabase().execSQL(deleteMealsSQL);

        // SQL-query for deleting the category
        String deleteCategorySQL = "DELETE FROM " + DatabaseContract.CategoryEntry.TABLE +
                " WHERE " + DatabaseContract.CategoryEntry.COLUMN_NAME + "='" + categoryName + "'";

        // execute query
        getWritableDatabase().execSQL(deleteCategorySQL);
    }

    public Category getCategory(String categoryName){

        ArrayList<Meal> meals = new ArrayList<>();

        String[] projection = {DatabaseContract.MealEntry.COLUMN_ID};

        String selection = DatabaseContract.MealEntry.COLUMN_CATEGORY + " = ?";
        String[] selectionArgs = {categoryName };

        Cursor cursor = null;
        try {
            cursor = getReadableDatabase().query(
                    DatabaseContract.MealEntry.TABLE,         // The table to query
                    projection,                               // The columns to return
                    selection,                                // The columns for the WHERE clause
                    selectionArgs,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            while (cursor.moveToNext()) {

                Meal meal = getMeal(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseContract.MealEntry.COLUMN_ID)));
                meals.add(meal);
            }
        }
        catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        finally {
            if(cursor != null)
                cursor.close();
        }

        return new Category(categoryName, meals);
    }

}
