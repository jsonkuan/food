package iths.com.food;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import iths.com.food.Helper.DatabaseHelper;
import iths.com.food.Model.Meal;

public class MealActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
    }

    public void onClickBtnSave(View view) {
        Meal meal = new Meal();
        meal.setName("Korv"); 

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.insertMeal(meal);
    }

    //TODO: Reusable view with editable and non-editable objects
}
