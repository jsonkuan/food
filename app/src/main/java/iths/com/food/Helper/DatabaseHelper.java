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

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        //values.put(DatabaseContract.MealEntry.COLUMN_ID, meal.getId());
        values.put(DatabaseContract.MealEntry.COLUMN_NAME, meal.getName());
        values.put(DatabaseContract.MealEntry.COLUMN_CATEGORY, meal.getCategory());
        values.put(DatabaseContract.MealEntry.COLUMN_DATE_TIME, meal.getDateTime());
        values.put(DatabaseContract.MealEntry.COLUMN_DESCRIPTION, meal.getDescription());
        values.put(DatabaseContract.MealEntry.COLUMN_HEALTHY_SCORE, meal.getHealthyScore());
        values.put(DatabaseContract.MealEntry.COLUMN_TASTE_SCORE, meal.getTasteScore());
        values.put(DatabaseContract.MealEntry.COLUMN_LONGITUDE, meal.getLongitude());
        values.put(DatabaseContract.MealEntry.COLUMN_LATITUDE, meal.getLatitude());
        values.put(DatabaseContract.MealEntry.COLUMN_IMAGE_PATH, meal.getImagePath());

        // Insert the new row, return the primary key value of the new row
        //setArrayListMeals(meal.getCategory());
        return getWritableDatabase().insert(DatabaseContract.MealEntry.TABLE, null, values);
    }

    public long insertCategory(String name){

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CategoryEntry.COLUMN_NAME, name);

        // Insert the new row, return the primary key value of the new row
        return getWritableDatabase().insert(DatabaseContract.CategoryEntry.TABLE, null, values);
    }

    public Meal getMeal(long id){
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
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

        // Filter results WHERE "id" = 'id'. The row we wont to return
        String selection = DatabaseContract.MealEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) }; // jag vill kanske skicka en long

        // How you want the results sorted in the resulting Cursor
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

            //populate the meal object
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
        //CategoryList categories = new CategoryList();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
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
        // New values for row
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.MealEntry.COLUMN_NAME, meal.getName());
        values.put(DatabaseContract.MealEntry.COLUMN_CATEGORY, meal.getCategory());
        //values.put(DatabaseContract.MealEntry.COLUMN_DATE_TIME, meal.getDateTime());
        values.put(DatabaseContract.MealEntry.COLUMN_DESCRIPTION, meal.getDescription());
        values.put(DatabaseContract.MealEntry.COLUMN_HEALTHY_SCORE, meal.getHealthyScore());
        values.put(DatabaseContract.MealEntry.COLUMN_TASTE_SCORE, meal.getTasteScore());
        //values.put(DatabaseContract.MealEntry.COLUMN_LONGITUDE, meal.getLongitude());
        //values.put(DatabaseContract.MealEntry.COLUMN_LATITUDE, meal.getLatitude());
        values.put(DatabaseContract.MealEntry.COLUMN_IMAGE_PATH, meal.getImagePath());

        // Which row to update, based on the id. WHERE "id" LIKE 'id'
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
        // Define 'where' part of query. WHERE "meal" LIKE '1'
        String selection = DatabaseContract.MealEntry.COLUMN_ID + " LIKE ?";

        // Specify arguments in placeholder order.
        String[] selectionArgs = { String.valueOf(id) };

        // Issue SQL statement. Returns one on success, zero otherwise
        return getWritableDatabase().delete(DatabaseContract.MealEntry.TABLE, selection, selectionArgs);
    }

    private Category getCategory(String categoryName){

        ArrayList<Meal> meals = new ArrayList<>();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {DatabaseContract.MealEntry.COLUMN_ID};

        // Filter results WHERE "categoryName" = 'categoryName'. The row we wont to return
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
