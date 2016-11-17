package iths.com.food;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import iths.com.food.Helper.DatabaseHelper;

public class MealCategoryActivity extends AppCompatActivity {

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_category_list);
        db = new DatabaseHelper(getApplicationContext());

    }

    public void deleteDatabase(View v) {
        Intent intent = new Intent(this, MealCategoryActivity.class);
        super.deleteDatabase("food.db");
        db = new DatabaseHelper(this);
        startActivity(intent);
        finish();
        Toast.makeText(getApplicationContext(), "Database deleted and restarted", Toast.LENGTH_SHORT).show();
    }
    public void createNewMeal(View v) {
        Intent intent = new Intent(this, MealActivity.class);
        startActivity(intent);
    }
}
