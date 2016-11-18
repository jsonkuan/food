package iths.com.food;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.jar.Attributes;

import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.Model.Category;
import iths.com.food.Model.CategoryList;
import iths.com.food.Model.Meal;

public class MealActivity extends AppCompatActivity {

    EditText name;
    EditText description;
    EditText category;
    RatingBar healthGrade;
    RatingBar tasteGrade;
    DatabaseHelper db;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        db = new DatabaseHelper(getApplicationContext());
        name = (EditText) findViewById(R.id.name);
        description = (EditText) findViewById(R.id.desc);
        category = (EditText)findViewById(R.id.category);
        healthGrade = (RatingBar) findViewById(R.id.rating_health);
        tasteGrade = (RatingBar) findViewById(R.id.rating_taste);

        ArrayList<Category> categories = db.getCategories();
        Toast.makeText(this, ""+categories.get(0).getAverageScore(), Toast.LENGTH_SHORT).show();
    }


    public void saveMeal(View view) {

        Meal meal = new Meal();

        meal.setHealthyScore((int)healthGrade.getRating());
        meal.setTasteScore((int)tasteGrade.getRating());
        meal.setName(name.getText().toString());
        meal.setDescription(description.getText().toString());
        meal.setCategory(category.getText().toString());
        meal.setDateTime(Calendar.getInstance().getTime().toString());
        meal.setLatitude(0);
        meal.setLongitude(0);
        meal.setImagePath("insert ImagePath");

        long id = db.insertMeal(meal);
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    public void showMeal(View view) {
        String text = ((EditText) findViewById(R.id.idOfMeal)).getText().toString();
        id = Long.valueOf(text);

        Meal meal = db.getMeal(id);

        name.setText(meal.getName());
        description.setText(meal.getDescription());
        category.setText(meal.getCategory());
        healthGrade.setRating(meal.getHealthyScore());
        tasteGrade.setRating(meal.getTasteScore());



    }

    public void deleteMeal(View view) {
        String text = ((EditText) findViewById(R.id.idOfMeal)).getText().toString();
        id = Long.valueOf(text);

        int rows = db.deleteMeal(id);

        if(rows > 0) {
            Toast.makeText(getApplicationContext(), "Deleted "+rows+" row(s)", Toast.LENGTH_SHORT).show();
        } else if (rows == 0) {
            Toast.makeText(getApplicationContext(), "No rows deleted", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateMeal(View view) {
        String text = ((EditText) findViewById(R.id.idOfMeal)).getText().toString();
        id = Long.valueOf(text);

        Meal meal = db.getMeal(id);
        meal.setHealthyScore((int)healthGrade.getRating());
        meal.setTasteScore((int)tasteGrade.getRating());
        meal.setName(name.getText().toString());
        meal.setDescription(description.getText().toString());
        meal.setCategory(category.getText().toString());
        meal.setDateTime(Calendar.getInstance().getTime().toString());
        meal.setLatitude(0);
        meal.setLongitude(0);
        meal.setImagePath("insert ImagePath");

        int rowsAffected = db.updateMeal(meal);

        if (rowsAffected > 0) {
            Toast.makeText(getApplicationContext(), rowsAffected+" rows updated", Toast.LENGTH_SHORT).show();
        }

    }


    //TODO: Reusable view with editable and non-editable objects
}
